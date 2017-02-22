package com.gh.yun

/**
  * Created by GH-GAN on 2016/6/3.
  */

import java.util

import com.gh.bean.appcapability._
import com.gh.bean.containercapability._
import com.gh.bean.logfile.{LogFileRedis, LogFileMySql, LogFileNginx, LogFileContainer}
import com.gh.utils.{ ConfigUtil, DateUtil, JsonUtil}
import com.gh.yun.parse.{ParseMysql, ParseRedis, ParseContainer, ParseNginx}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext._
import org.elasticsearch.spark.rdd.EsSpark

import org.elasticsearch.spark.streaming._
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.codehaus.jackson.JsonNode
import org.elasticsearch.spark._

import scala.collection.mutable.ArrayBuffer
import scala.collection.{mutable, Seq}

object SaveToESCap {
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

   /* val datas = stream.map(line => {
      var node : JsonNode = JsonUtil.getJsonNode(line.value())
      (node,line.value())
    }).filter(x => x._1 != null && x._1.get("type") != null)
      .map(node => (node._1.get("type").asText(),node._2))*/

    val datas = stream.map(line => {
      var node : JsonNode = JsonUtil.getJsonNode(line.value())
      node
    }).filter(x => x != null && x.get("type") != null)
      .map(node => (node.get("type").asText(),node))

    // cap
    ParseAppMysqlSaveToEs2(datas)  // appmysql
    ParseAppRedisSaveToEs2(datas)  // appredis
    ParseAppNginxSaveToEs2(datas)   // appnginx
    ParseContainerSaveToEs2(datas)  // container

//     datas.foreachRDD(x => {
//      x.foreach(y => println(y._1 + "============================================" + y._2))
//    })

    ssc.start()
    ssc.awaitTermination()

  }

  def ParseAppNginxSaveToEs(datas : DStream[(String,String)]): Unit ={
    val nginx = datas.filter(_._1.equals("nginx"))
    nginx.flatMap(lines => {
      var node : JsonNode = JsonUtil.getJsonNode(lines._2)
      new ParseNginx().statsToArray(lines._1,node)
    }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-app-nginx-{data.stats.timestamp:YYYY-MM-dd}/nginx-cap")
  }

  def ParseAppNginxSaveToEs2(datas : DStream[(String,JsonNode)]): Unit ={
    val nginx = datas.filter(_._1.equals("nginx"))
    nginx.flatMap(lines => {
      new ParseNginx().statsToArray(lines._1,lines._2)
    }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-app-nginx-{data.stats.timestamp:YYYY-MM-dd}/nginx-cap",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

  def ParseAppMysqlSaveToEs(datas : DStream[(String,String)]): Unit ={
    val mysql = datas.filter(_._1.equals("mysql"))
    mysql.flatMap(lines => {
        val appMySql = JsonUtil.getBean(lines._2, classOf[AppMySql])
        val _stats = appMySql.getData().getStats
        val arr = ArrayBuffer[(String)]()

        if (!"".equals(appMySql.getData.getContainer_uuid) && !"".equals(appMySql.getData.getEnvironment_id)){
          for (i <- 0 to (_stats.size() - 1) ){
            val stats_i = _stats.get(i)

            var ms = new AppMySql2()
            var amsd = new AppMySqlData2()
            amsd.setContainer_name(appMySql.getData.getContainer_name)
            amsd.setContainer_uuid(appMySql.getData.getContainer_uuid)
            amsd.setEnvironment_id(appMySql.getData.getEnvironment_id)
            amsd.setNamespace(appMySql.getData.getNamespace)
            amsd.setTimestamp(appMySql.getData.getTimestamp)
            amsd.setStats(stats_i)
            ms.setType(appMySql.getType)
            ms.setData(amsd)

            arr.+=(JsonUtil.formatJson(ms))
          }
        }
        arr
      }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-app-mysql-{data.stats.timestamp:YYYY-MM-dd}/mysql-cap")
  }

  def ParseAppMysqlSaveToEs2(datas : DStream[(String,JsonNode)]): Unit ={
    val mysql = datas.filter(_._1.equals("mysql"))
    val s = mysql.flatMap(lines => {
      new ParseMysql().statsToArray(lines._1,lines._2)
    }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-app-mysql-{data.stats.timestamp:YYYY-MM-dd}/mysql-cap",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

  def ParseAppRedisSaveToEs(datas : DStream[(String,String)]): Unit ={
    val redis = datas.filter(_._1.equals("redis"))
    redis.flatMap(lines => {
        val appRedis = JsonUtil.getBean(lines._2, classOf[AppRedis])
        val _stats = appRedis.getData.getStats
        val arr = ArrayBuffer[(String)]()

        if (!"".equals(appRedis.getData.getEnvironment_id) && !"".equals(appRedis.getData.getContainer_uuid)){
          for (i <- 0 to (_stats.size() - 1) ){
            val stats_i = _stats.get(i)
            var ar2 = new AppRedis2()
            var ard2 = new AppRedisData2
            ard2.setContainer_name(appRedis.getData.getContainer_name)
            ard2.setContainer_uuid(appRedis.getData.getContainer_uuid)
            ard2.setEnvironment_id(appRedis.getData.getEnvironment_id)
            ard2.setNamespace(appRedis.getData.getNamespace)
            ard2.setTimestamp(appRedis.getData.getTimestamp)
            ard2.setStats(stats_i)

            ar2.setType(appRedis.getType)
            ar2.setData(ard2)

            arr.+=(JsonUtil.formatJson(ar2))
          }
        }
        arr
      }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-app-redis-{data.stats.timestamp:YYYY-MM-dd}/redis-cap")
  }

  def ParseAppRedisSaveToEs2(datas : DStream[(String,JsonNode)]): Unit ={
    val redis = datas.filter(_._1.equals("redis"))
    redis.flatMap(lines => {
      new ParseRedis().statsToArray(lines._1,lines._2)
    }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-app-redis-{data.stats.timestamp:YYYY-MM-dd}/redis-cap",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

  def ParseContainerSaveToEs(datas : DStream[(String,String)]): Unit ={
    val container = datas.filter(_._1.equals("container"))
    container.flatMap(lines => {
        val container = JsonUtil.getBean(lines._2, classOf[Container])
        val datas = container.getData  // containers

        val _containers = datas.flatMap(data => {      // data
          val timestamp = data.getTimestamp
          val environment_id = data.getEnvironment_id.trim
          val container_uuid = data.getContainer_uuid.trim

          if (!"".equals(environment_id) && !"".equals(container_uuid)){
            val _stats = data.getStats   // 一个容器多条记录
            val ccs = _stats.flatMap(cont => {
                var container2 = new Container2()
                var cd2 = new ContainerData2()
                cd2.setStats(cont)
                cd2.setContainer_name(data.getContainer_name)
                cd2.setContainer_uuid(container_uuid)
                cd2.setEnvironment_id(environment_id)
                cd2.setNamespace(data.getNamespace)
                cd2.setTimestamp(data.getTimestamp)
                container2.setType(container.getType)
                container2.setData(cd2)
                Array(JsonUtil.formatJson(container2))
              })
            ccs
          }else{
            Array[String]()
          }
        })
        _containers
      }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-container-container-{data.stats.timestamp:YYYY-MM-dd}/container-cap")
  }

  def ParseContainerSaveToEs2(datas : DStream[(String,JsonNode)]): Unit ={
    val container = datas.filter(_._1.equals("container"))
    val ss = container.flatMap(lines => {
      new ParseContainer().statsToArray(lines._1,lines._2)
    }).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-container-container-{data.stats.timestamp:YYYY-MM-dd}/container-cap",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))

  }

}
