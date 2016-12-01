package com.gh.yun

import com.gh.bean.{AlertData, AlertDataInfo}
import com.gh.utils.{HttpUtil, FormatUtil, JsonUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Logger, Level}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.codehaus.jackson.JsonNode

import scala.collection.immutable.HashMap

/**
  * Created by GH-GAN on 2016/11/24.
  */
object AppMysqlCap {
  def main(args: Array[String]) {
   /* if (args.length < 4) {
      System.err.println("Usage: Kafka <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }*/
    Logger.getRootLogger.setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("capability-mysql").setMaster("local")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("D:\\tmp\\checkpoint")

    var brokers ="192.168.100.180:8074,192.168.100.181:8074,192.168.100.182:8074"
    val _topics = "capability-mysql".split(",").toSet
    val group = "capability-mysql-js"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",          //当前偏移不在服务器上时,按最新开始
//      "fetch.max.bytes" -> 524288000,            // 最大获取字节大小
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](_topics, kafkaParams)
    )

    val datas = parseLog(stream)
    val mysql_group = datas.groupByKey()
    compute(mysql_group)

    ssc.start()
    ssc.awaitTermination()
  }

  def parseLog(stream : InputDStream[ConsumerRecord[String, String]]) : DStream[(String,(String,Double,Double))] ={
    val datas = stream.map(line => {
        var node : JsonNode = null
        try{
          node = JsonUtil.getJsonNode(line.value())
        }catch {
          case ex : Exception => {
            ex.printStackTrace()
            ("",("",0.0,0.0))
          }
        }

        val _type = node.get("type").asText()
        val environment_id = node.get("data").get("environment_id").asText()
        val container_uuid = node.get("data").get("container_uuid").asText()

        val stats = node.get("data").get("stats").get(0)

        val timestamp = stats.get("timestamp").asText()
        val thread_connected = stats.get("thread_connected").asDouble()
        val max_connections = stats.get("max_connections").asDouble()

        (environment_id+"#"+container_uuid+"#"+_type, (timestamp,thread_connected,max_connections) )

    })
    datas
  }

  def compute(mysql_group : DStream[(String,Iterable[(String,Double,Double)])]): Unit ={
      val warn = mysql_group.map(x => {
  //        if ("".equals(key)) ("","","",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
          val count = x._2.size

          // thread_connected
          val thread_connected_sum = x._2.map(_._2).reduce(_+_)
          val thread_connected_avg = thread_connected_sum / count

          val start_log = x._2.head
          val end_log = x._2.last
          val start_time = start_log._1
          val end_time = end_log._1
          val max_connections = start_log._3

          // thread_connected_avg / max_connections
          val con_threshold = thread_connected_avg / max_connections

          (x._1,start_time,end_time,con_threshold)
      }).filter(_._4 > 0.7)   // 超过阈值

      warn.foreachRDD( record => {
        val alerts = record.map(line => {
          FormatUtil.toMySqlWarnJson(line._1, line._2, line._3, line._4)
        })
        val collect = alerts.collect()
        if (collect.size > 0)  httpPost(collect)
      })

  }

  //告警
  def httpPost(alerts : Array[String]): Unit ={
    val ad = new AlertData()
    ad.setAlert_data(alerts)
    var json = JsonUtil.formatJson(ad)

//    HttpUtil.Post("",json)
    println(json)
  }

}
