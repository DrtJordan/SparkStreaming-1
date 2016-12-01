package com.gh.yun

/**
  * Created by GH-GAN on 2016/6/3.
  */

import com.gh.bean.appcapability.{AppMysqlStats, AppMySql, AppMySqlData}
import com.gh.utils.{DateUtil, JsonUtil}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext._
import org.elasticsearch.spark.streaming._
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.codehaus.jackson.JsonNode
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.EsSpark

import scala.collection.{Seq}

object SaveToES {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[2]").setAppName("saveToes");
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", "192.168.100.154:8070,192.168.100.162:8070,192.168.100.163:8070,192.168.100.164:8070," +
      "192.168.100.166:8070,192.168.100.167:8070,192.168.100.168:8070,192.168.100.169:8070")

    val ssc = new StreamingContext(conf,Seconds(5))
    ssc.checkpoint("D:\\tmp\\checkpoint")

    var brokers ="192.168.100.180:8074,192.168.100.181:8074,192.168.100.182:8074"
    val _topics = "capability-mysql,capability-redis,capability-container,log-file".split(",").toSet
    val group = "saveToes"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",
      //      "fetch.max.bytes" -> 524288000,            // 最大获取字节大小
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](_topics, kafkaParams)
    )

    val datas = stream.map(line => {
      var node : JsonNode = null
      try{
        node = JsonUtil.getJsonNode(line.value())
        val _type = node.get("type").asText()
        (_type, line.value())
      }catch {
        case ex : Exception => ex.printStackTrace()
      }
      ("","")
    })

    // appmysql
    ParseAppMysqlSaveToEs(datas)


    datas.foreachRDD(x => {
      x.foreach(y => println(y._1 + "=" + y._2))
    })


    ssc.start()
    ssc.awaitTermination()

  }

  def ParseAppMysqlSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("mysql"))
      .map(lines => {
        val appMySql = JsonUtil.getBean(lines._2, classOf[AppMySql])
        val mysql = appMySql.getData().getStats.get(0)
        val timestamp = mysql.getTimestamp
        val _timestamp = DateUtil.formatToUTC_0(timestamp)
        mysql.setTimestamp(_timestamp)
        appMySql.setType(appMySql.getType + "-" + DateUtil.getyyyyMMdd(_timestamp))
        JsonUtil.formatJson(appMySql)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }


}
