package com.gh.yun

import java.util

import com.gh.bean.alert.{KeyValue, AlertDataInfo, AlertData}
import com.gh.bean.containercapability.ContainerFileSystem
import com.gh.utils._
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

import scala.collection.mutable.ArrayBuffer

/**
  * Created by GH-GAN on 2016/11/24.
  */
object AppRedisCap {
  def main(args: Array[String]) {

    Logger.getRootLogger.setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("capability-redis").setMaster(ConfigUtil.sparkmaster)
    val ssc = new StreamingContext(sparkConf, Seconds(ConfigUtil.capStreamtime))
    ssc.checkpoint(ConfigUtil.redisCapcheckpoint)

    var brokers = ConfigUtil.brokers
    val _topics = "capability-redis".split(",").toSet
    val group = "capability-redis-js"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",          //当前偏移不在服务器上时,按最新开始
      "heartbeat.interval.ms" -> "6000",
      "session.timeout.ms" -> "20000",
      "max.partition.fetch.bytes" -> "1048576000",
      "max.poll.records" -> "5000000",                 // message.max.bytes
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](_topics, kafkaParams)
    )

    val datas = parseLog2(stream)
    val redis_group = datas.groupByKey()
    compute(redis_group)

    ssc.start()
    ssc.awaitTermination()
  }

  def parseLog2(stream : InputDStream[ConsumerRecord[String, String]]) : DStream[(String,(String,Double,Double,Double,Double))] ={
    val datas = stream.map(line => {
      var node : JsonNode = JsonUtil.getJsonNode(line.value())
      node
    }).filter(x => (x != null && x.get("type") != null))
      .filter(x => {
        val _type = x.get("type").asText().trim
        var environment_id = ""
        var container_uuid = ""
        try{
          val data = x.get("data")
          environment_id = data.get("environment_id").asText().trim
          container_uuid = data.get("container_uuid").asText().trim
        }catch { case ex : Exception => ex.printStackTrace() }

        !"".equals(_type) && !"".equals(environment_id) && !"".equals(container_uuid) && null != environment_id && null != container_uuid
      })
      .flatMap(node => {
      val _type = node.get("type").asText()
      val environment_id = node.get("data").get("environment_id").asText()
      val container_uuid = node.get("data").get("container_uuid").asText()

      var container_name = ""
      var namespace = ""
      try {
        container_name = node.get("data").get("container_name").asText()
        namespace = node.get("data").get("namespace").asText()
      }catch {
        case ex : Exception => {
          println("--------> container_name/namespace is null")
          ex.printStackTrace()
        }
      }

      val _stats = node.get("data").get("stats")
      val arr = ArrayBuffer[(String,(String,Double,Double,Double,Double))]()
      for (i <- 0 to (_stats.size() - 1) ){
        val stats = node.get("data").get("stats").get(i)
        val timestamp = stats.get("timestamp").asText()
        val used_memory = stats.get("used_memory").asDouble()
        val max_memory = stats.get("max_memory").asDouble()
        val keyspace_hits = stats.get("keyspace_hits").asDouble()
        val keyspace_misses = stats.get("keyspace_misses").asDouble()
        arr.+=((environment_id+"#"+container_uuid+"#"+_type + "#" + container_name + "#" + namespace, (timestamp,used_memory,max_memory,keyspace_hits,keyspace_misses)))
      }
      arr
    })
    datas
  }

  def parseLog(stream : InputDStream[ConsumerRecord[String, String]]) : DStream[(String,(String,Double,Double,Double,Double))] ={
    val datas = stream.map(line => {
      var node : JsonNode = JsonUtil.getJsonNode(line.value())
      node
    }).filter(x => (x != null && x.get("type") != null)).map(node => {
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

          var memory_threshold = - 1.0
          if (0 != max_memory && null != max_memory){
            // 占用内存
             memory_threshold = used_memory_avg / max_memory
          }

          // 命中率
          val hit_threshold = ( end_log._4 - start_log._4 ) / ((end_log._4 + end_log._5) - (start_log._4 + start_log._5))

          (x._1,start_time,end_time,memory_threshold,hit_threshold)
      }).map(aAlert(_))
        .filter(_._4.size() > 0)  // 是否有告警信息
   /* warn.filter(x => {
      HttpUtil.URL match {
        case "" => x._4 > 0.8 || x._5 > 0.6
        case "" => (x._4 > 0.8 || x._5 > 0.6)
      }
    })*/

      warn.foreachRDD( record => {
        val alerts = record.map(line => {
          AlertInfoUtil.toWarnBean(AlertInfoUtil.SUCCESS,AlertInfoUtil.ALERT_TYPE_M,AlertInfoUtil.ALERT_DIM_A,line._1, line._2,line._3,line._4,line._5)
        })
        if (!alerts.isEmpty()){
          val collect = alerts.collect()
          if (collect.size > 0)  new HttpUtil().alerts(collect)  //告警
        }
      })

  }

  def aAlert(line : (String,String,String,Double,Double)): (String,String,String,util.ArrayList[KeyValue],String) ={
    val list = new util.ArrayList[KeyValue]()

//    if (line._4 > 0.8) list.add(new KeyValue("memory",line._4.toString))
//    if (line._5 > 0.6) list.add(new KeyValue("hits",line._5.toString))

    val memory = HttpUtil.gz_map.get("app_redis_memory")
    memory.getCondition match {
      case "GTE"  =>   if(-1 != line._4 && line._4 >= memory.getValue)    list.add(new KeyValue("memory",line._4.toString))
      case "GT"   =>   if(-1 != line._4 && line._4  >  memory.getValue)   list.add(new KeyValue("memory",line._4.toString))
      case "LTE"  =>   if(-1 != line._4 && line._4  <= memory.getValue)   list.add(new KeyValue("memory",line._4.toString))
      case "LT"   =>   if(-1 != line._4 && line._4  <  memory.getValue)   list.add(new KeyValue("memory",line._4.toString))
      case "EQ"   =>   if(-1 != line._4 && line._4  == memory.getValue)   list.add(new KeyValue("memory",line._4.toString))
    }

    val hits = HttpUtil.gz_map.get("app_redis_hits")
    hits.getCondition match {
      case "GTE"  =>   if(line._5 >=   hits.getValue)   list.add(new KeyValue("hits",line._5.toString))
      case "GT"   =>   if(line._5  >   hits.getValue)   list.add(new KeyValue("hits",line._5.toString))
      case "LTE"  =>   if(line._5  <=  hits.getValue)   list.add(new KeyValue("hits",line._5.toString))
      case "LT"   =>   if(line._5  <   hits.getValue)   list.add(new KeyValue("hits",line._5.toString))
      case "EQ"   =>   if(line._5  ==  hits.getValue)   list.add(new KeyValue("hits",line._5.toString))
    }

//    list.add(new KeyValue("memory",line._4.toString))

    (line._1,line._2,line._3,list,"")
  }

}
