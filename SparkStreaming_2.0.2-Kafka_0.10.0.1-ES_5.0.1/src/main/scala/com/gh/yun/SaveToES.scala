package com.gh.yun

/**
  * Created by GH-GAN on 2016/6/3.
  */

import com.gh.bean.appcapability.{AppRedis, AppMysqlStats, AppMySql, AppMySqlData}
import com.gh.bean.logfile.{LogFileRedis, LogFileMySql, LogFileNginx, LogFileContainer}
import com.gh.utils.{DateUtil, JsonUtil}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
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
    Logger.getRootLogger.setLevel(Level.WARN)
    val conf = new SparkConf().setMaster("local[2]").setAppName("saveToes");
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", "192.168.100.154:8070,192.168.100.162:8070,192.168.100.163:8070,192.168.100.164:8070," +
      "192.168.100.166:8070,192.168.100.167:8070,192.168.100.168:8070,192.168.100.169:8070")

    val ssc = new StreamingContext(conf,Seconds(60))
    ssc.checkpoint("D:\\tmp\\checkpoint2")

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
      }catch {
        case ex : Exception => ex.printStackTrace()
      }
      (node,line.value())
    }).filter(_._1 != null).map(node => (node._1.get("type").asText(),node._2))

    // cap
    ParseAppMysqlSaveToEs(datas)  // appmysql
    ParseAppRedisSaveToEs(datas)  // appredis
//    ParseContainerSaveToEs(datas)  // container

    // log
    ParseLogContainerSaveToEs(datas)  // log_container
    ParseLogNginxSaveToEs(datas)      // log_nginx
    ParseLogMySqlSaveToEs(datas)      // log_mysql
    ParseLogRedisSaveToEs(datas)      // log_redis

    /*datas.foreachRDD(x => {
      x.foreach(y => println(y._1 + "=" + y._2))
    })*/

    ssc.start()
    ssc.awaitTermination()

  }

  def ParseAppMysqlSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("monitor_mysql"))
      .map(lines => {
        val appMySql = JsonUtil.getBean(lines._2, classOf[AppMySql])
        val stats_0 = appMySql.getData().getStats.get(0)
        val timestamp = stats_0.getTimestamp
        val _timestamp = DateUtil.formatToUTC_0(timestamp)
        stats_0.setTimestamp(_timestamp)
        appMySql.setType(appMySql.getType + "-" + DateUtil.getyyyyMMdd(_timestamp))
        JsonUtil.formatJson(appMySql)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }

  def ParseAppRedisSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("monitor_redis"))
      .map(lines => {
        val appRedis = JsonUtil.getBean(lines._2, classOf[AppRedis])
        val stats_0 = appRedis.getData().getStats.get(0)
        val timestamp = stats_0.getTimestamp
        val _timestamp = DateUtil.formatToUTC_0(timestamp)
        stats_0.setTimestamp(_timestamp)
        appRedis.setType(appRedis.getType + "-" + DateUtil.getyyyyMMdd(_timestamp))
        JsonUtil.formatJson(appRedis)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }

  def ParseLogContainerSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("log_container"))
      .map(lines => {
        val logFileContainer = JsonUtil.getBean(lines._2, classOf[LogFileContainer])
        val data = logFileContainer.getData
        val log_info = logFileContainer.getData.getLog_info
        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
        log_info.setLog_time(_logtimestamp)
        logFileContainer.setType(logFileContainer.getType + "-" + DateUtil.getyyyyMMdd(_logtimestamp))
        JsonUtil.formatJson(logFileContainer)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }

  def ParseLogNginxSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("log_nginx"))
      .map(lines => {
        val logFileNginx = JsonUtil.getBean(lines._2, classOf[LogFileNginx])
        val data = logFileNginx.getData
        val log_info = logFileNginx.getData.getLog_info
        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
        log_info.setLog_time(_logtimestamp)
        logFileNginx.setType(logFileNginx.getType + "-" + DateUtil.getyyyyMMdd(_logtimestamp))
        JsonUtil.formatJson(logFileNginx)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }

  def ParseLogMySqlSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("log_mysql"))
      .map(lines => {
        val logFileMySql = JsonUtil.getBean(lines._2, classOf[LogFileMySql])
        val data = logFileMySql.getData
        val log_info = logFileMySql.getData.getLog_info
        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
        log_info.setLog_time(_logtimestamp)
        logFileMySql.setType(logFileMySql.getType + "-" + DateUtil.getyyyyMMdd(_logtimestamp))
        JsonUtil.formatJson(logFileMySql)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }

  def ParseLogRedisSaveToEs(datas : DStream[(String,String)]): Unit ={
    datas.filter(_._1.equals("log_redis"))
      .map(lines => {
        val logFileRedis = JsonUtil.getBean(lines._2, classOf[LogFileRedis])
        val data = logFileRedis.getData
        val log_info = logFileRedis.getData.getLog_info
        data.setTimestamp(DateUtil.formatToUTC_0(data.getTimestamp))
        val _logtimestamp =  DateUtil.formatToUTC_0(log_info.getLog_time)
        log_info.setLog_time(_logtimestamp)
        logFileRedis.setType(logFileRedis.getType + "-" + DateUtil.getyyyyMMdd(_logtimestamp))
        JsonUtil.formatJson(logFileRedis)
      }).saveJsonToEs("{data.environment_id}-{data.container_uuid}-{type}/{type}")
  }
}
