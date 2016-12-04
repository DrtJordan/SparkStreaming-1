package com.gh.yun

import java.util

import com.gh.bean.alert.{KeyValue, AlertDataInfo, AlertData}
import com.gh.utils.{HttpUtil, AlertInfoUtil, JsonUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.codehaus.jackson.JsonNode

/**
  * Created by GH-GAN on 2016/11/24.
  */
object AppRedisCap {
  def main(args: Array[String]) {
   /* if (args.length < 4) {
      System.err.println("Usage: Kafka <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }*/
    Logger.getRootLogger.setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("capability-redis").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("D:\\tmp\\checkpoint")

    var brokers ="192.168.100.180:8074,192.168.100.181:8074,192.168.100.182:8074"
    val _topics = "capability-redis".split(",").toSet
    val group = "capability-redis-js"

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
    val redis_group = datas.groupByKey()
    compute(redis_group)

    ssc.start()
    ssc.awaitTermination()
  }

  def parseLog(stream : InputDStream[ConsumerRecord[String, String]]) : DStream[(String,(String,Double,Double,Double,Double))] ={
    val datas = stream.map(line => {
        var node : JsonNode = null
        try{
          node = JsonUtil.getJsonNode(line.value())
        }catch {
          case ex : Exception => ex.printStackTrace()
        }
      node
    }).filter(_ != null).map(node => {
        val _type = node.get("type").asText()
        val environment_id = node.get("data").get("environment_id").asText()
        val container_uuid = node.get("data").get("container_uuid").asText()

        val stats = node.get("data").get("stats").get(0)

        val timestamp = stats.get("timestamp").asText()
        val used_memory = stats.get("used_memory").asDouble()
        val max_memory = stats.get("max_memory").asDouble()
        val keyspace_hits = stats.get("keyspace_hits").asDouble()
        val keyspace_misses = stats.get("keyspace_misses").asDouble()

        (environment_id+"#"+container_uuid+"#"+_type, (timestamp,used_memory,max_memory,keyspace_hits,keyspace_misses) )
    })
    datas
  }

  def compute(redis_group : DStream[(String,Iterable[(String,Double,Double,Double,Double)])]): Unit ={
      val warn = redis_group.map(x => {
          val count = x._2.size

          val start_log = x._2.head
          val end_log = x._2.last
          val start_time = start_log._1
          val end_time = end_log._1

          // used_memory
          val used_memory_sum = x._2.map(_._2).reduce(_+_)
          val used_memory_avg = used_memory_sum / count

          //max_memory
          val max_memory = start_log._3

          // 占用内存
          val memory_threshold = used_memory_avg / max_memory
          // 命中率
          val hit_threshold = ( end_log._4 - start_log._4 ) / ((end_log._4 + end_log._5) - (start_log._4 + start_log._5))

          (x._1,start_time,end_time,memory_threshold,hit_threshold)
      }).filter(x => (x._4 > 0.8 || x._5 > 0.6))   // 超过阈值

   /* warn.filter(x => {
      HttpUtil.URL match {
        case "" => x._4 > 0.8 || x._5 > 0.6
        case "" => (x._4 > 0.8 || x._5 > 0.6)
      }
    })*/

      warn.foreachRDD( record => {
        val alerts = record.map(line => {
          val list = new util.ArrayList[KeyValue]()
          list.add(new KeyValue("memory_threshold",line._4.toString))
          list.add(new KeyValue("hit_threshold",line._5.toString))
          AlertInfoUtil.toWarnBean(AlertInfoUtil.SUCCESS,AlertInfoUtil.ALERT_TYPE_M,AlertInfoUtil.ALERT_DIM_A,line._1, line._2, line._3, list)
        })
        val collect = alerts.collect()
        if (collect.size > 0)  httpPost(collect)
      })

  }

  //告警
  def httpPost(alerts : Array[AlertDataInfo]): Unit ={
    val ad = new AlertData().setAlert_data(alerts)
    var json = JsonUtil.formatJson(ad)
    HttpUtil.Post(json)
    println(json)
  }

}
