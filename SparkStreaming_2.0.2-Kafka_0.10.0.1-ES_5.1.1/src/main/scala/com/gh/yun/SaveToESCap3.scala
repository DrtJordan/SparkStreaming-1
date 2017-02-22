package com.gh.yun

/**
  * Created by GH-GAN on 2016/6/3.
  * 保存入es时，将app和容器只分为两个队列
  * 整理index，indexName： yun-环境id-日志类型-时间/容器id
  */

import com.gh.utils.{ConfigUtil, JsonUtil}
import com.gh.yun.parse.{ParseContainer, ParseMysql, ParseNginx, ParseRedis}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.codehaus.jackson.JsonNode
import org.elasticsearch.spark.streaming._

import scala.collection.mutable.ArrayBuffer

object SaveToESCap3 {
  def main(args: Array[String]) {
    Logger.getRootLogger.setLevel(Level.WARN)
    val conf = new SparkConf().setMaster(ConfigUtil.sparkmaster).setAppName("saveToesCap");
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", ConfigUtil.esnodes)
    conf.set("spark.streaming.kafka.consumer.poll.ms", "1024")

    val ssc = new StreamingContext(conf,Seconds(ConfigUtil.capStreamSaveTime))
    ssc.checkpoint(ConfigUtil.saveCapcheckpoint)

    var brokers = ConfigUtil.brokers
    val _topics = "capability-mysql,capability-redis,capability-container,capability-nginx".split(",").toSet
//    val _topics = "capability-container".split(",").toSet
    val group = "saveToesCap"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",
      "heartbeat.interval.ms" -> "6000",
      "session.timeout.ms" -> "20000",
      "max.partition.fetch.bytes" -> "10485760",
      "max.poll.records" -> "10000",                 // message.max.bytes
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](_topics, kafkaParams)
    )

    val datas = stream.map(line => {
      var node : JsonNode = JsonUtil.getJsonNode(line.value())
      node
    }).filter(x => x != null && x.get("type") != null)
      .map(node => (node.get("type").asText(),node))

    // cap
    ParseContainerSaveToEs(datas)
    ParseAppSaveToEs(datas)

//     datas.foreachRDD(x => {
//      x.foreach(y => println(y._1 + "============================================" + y._2))
//    })

    ssc.start()
    ssc.awaitTermination()

  }

  def ParseAppSaveToEs(datas : DStream[(String,JsonNode)]): Unit ={
    val app = datas.filter(x => x._1.equals("nginx") || x._1.equals("mysql") || x._1.equals("redis"))
    app.flatMap(lines => {
      val arr = ArrayBuffer[String]()
      if (lines._1.equals("nginx")){
        arr.++=(new ParseNginx().statsToArray(lines._1,lines._2))
      }
      if (lines._1.equals("mysql")){
        arr.++=(new ParseMysql().statsToArray(lines._1,lines._2))
      }
      if (lines._1.equals("redis")){
        arr.++=(new ParseRedis().statsToArray(lines._1,lines._2))
      }
      arr
    }).saveJsonToEs("yun-{data.environment_id}-app-{type}-{data.stats.timestamp:YYYY-MM-dd}/{data.container_uuid}",Map("es.batch.size.bytes"->"1048576","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

  def ParseContainerSaveToEs(datas : DStream[(String,JsonNode)]): Unit ={
    val container = datas.filter(x => x._1.equals("container"))
    val ss = container.flatMap(lines => {
      new ParseContainer().statsToArray(lines._1,lines._2)
    }).saveJsonToEs("yun-{data.environment_id}-container-container-{data.stats.timestamp:YYYY-MM-dd}/{data.container_uuid}",Map("es.batch.size.bytes"->"1048576","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))

  }

}
