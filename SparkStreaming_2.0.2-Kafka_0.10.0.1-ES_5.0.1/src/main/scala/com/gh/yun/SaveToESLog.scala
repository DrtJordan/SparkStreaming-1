package com.gh.yun

/**
  * Created by GH-GAN on 2016/6/3.
  */

import java.util

import com.gh.bean.appcapability._
import com.gh.bean.containercapability._
import com.gh.bean.logfile.{LogFileContainer, LogFileMySql, LogFileNginx, LogFileRedis}
import com.gh.utils.{ConfigUtil, DateUtil, JsonUtil}
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
import org.elasticsearch.spark._
import scala.collection.mutable.ArrayBuffer

object SaveToESLog {
  def main(args: Array[String]) {
    Logger.getRootLogger.setLevel(Level.WARN)
    val conf = new SparkConf().setMaster(ConfigUtil.sparkmaster).setAppName("saveToesLog");
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", ConfigUtil.esnodes)

    val ssc = new StreamingContext(conf,Seconds(ConfigUtil.logStreamSaveTime))
    ssc.checkpoint(ConfigUtil.saveLogcheckpoint)

    var brokers = ConfigUtil.brokers
    val _topics = "log-file".split(",").toSet
    val group = "saveToesLog"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",
      "heartbeat.interval.ms" -> "6000",
      "session.timeout.ms" -> "20000",
      "max.partition.fetch.bytes" -> "10485760",   //1048576000
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
      (node,line.value())
    }).filter(x => x._1 != null && x._1.get("type") != null)
      //environment_id/container_uuid not null/""
      .filter(x => {
          var environment_id = ""
          var container_uuid = ""
          try{
              val data = x._1.get("data")
              environment_id = data.get("environment_id").asText()
              container_uuid = data.get("container_uuid").asText()
          }catch { case ex : Exception => ex.printStackTrace() }

          !"".equals(environment_id) && !"".equals(container_uuid)
       })
      .map(node => (node._1.get("type").asText(),node._2))

    // log
    ParseLogContainerSaveToEs2(datas)  // log_container
    ParseLogNginxSaveToEs2(datas)      // log_nginx
    ParseLogMySqlSaveToEs2(datas)      // log_mysql
    ParseLogRedisSaveToEs2(datas)      // log_redis

    /*datas.foreachRDD(x => {
      x.foreach(y => println(y))
    })*/

    ssc.start()
    ssc.awaitTermination()

  }

  def ParseLogContainerSaveToEs2(datas : DStream[(String,String)]): Unit ={
    val container = datas.filter(_._1.equals("container"))
    container.map(_._2).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-containerlogfile-container-{data.log_info.log_time:YYYY-MM-dd}/container-log",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

  /*def ParseLogContainerSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("container"))
      .map(lines => {
        val logFileContainer = JsonUtil.getBean(lines._2, classOf[LogFileContainer])
        val data = logFileContainer.getData
        val log_info = logFileContainer.getData.getLog_info
//        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
//        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
//        log_info.setLog_time(_logtimestamp)
        logFileContainer.setType(lines._1 + "-" + DateUtil.getyyyyMMdd(log_info.getLog_time))
        JsonUtil.formatJson(logFileContainer)
      }).filter(_ != null).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-containerlogfile-{type}/{type}")
  }*/

  def ParseLogNginxSaveToEs2(datas : DStream[(String,String)]): Unit ={
    val nginx = datas.filter(_._1.equals("nginx"))
    nginx.map(_._2).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-applogfile-nginx-{data.log_info.log_time:YYYY-MM-dd}/nginx-log",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

 /* def ParseLogNginxSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("nginx"))
      .map(lines => {
        val logFileNginx = JsonUtil.getBean(lines._2, classOf[LogFileNginx])
        val data = logFileNginx.getData
        val log_info = logFileNginx.getData.getLog_info
//        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
//        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
//        log_info.setLog_time(_logtimestamp)
        logFileNginx.setType(lines._1 + "-" + DateUtil.getyyyyMMdd(log_info.getLog_time))
        JsonUtil.formatJson(logFileNginx)
      }).filter(_ != null).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-applogfile-{type}/{type}")
  }*/

  def ParseLogMySqlSaveToEs2(datas : DStream[(String,String)]): Unit ={
    val mysql = datas.filter(_._1.equals("mysql"))
    mysql.map(_._2).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-applogfile-mysql-{data.log_info.log_time:YYYY-MM-dd}/mysql-log",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
  }

/*  def ParseLogMySqlSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("mysql"))
      .map(lines => {
        val logFileMySql = JsonUtil.getBean(lines._2, classOf[LogFileMySql])
        val data = logFileMySql.getData
        val log_info = logFileMySql.getData.getLog_info
//        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
//        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
//        log_info.setLog_time(_logtimestamp)
        logFileMySql.setType(lines._1 + "-" + DateUtil.getyyyyMMdd(log_info.getLog_time))
        JsonUtil.formatJson(logFileMySql)
      }).filter(_ != null).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-applogfile-{type}/{type}")
  }*/


    def ParseLogRedisSaveToEs2(datas : DStream[(String,String)]): Unit ={
      val redis = datas.filter(_._1.equals("redis"))
      redis.map(_._2).saveJsonToEs("yun-{data.environment_id}-{data.container_uuid}-applogfile-redis-{data.log_info.log_time:YYYY-MM-dd}/redis-log",Map("es.batch.size.bytes"->"2097152","es.batch.size.entries"->"500","es.batch.write.refresh"->"false"))
    }

}
