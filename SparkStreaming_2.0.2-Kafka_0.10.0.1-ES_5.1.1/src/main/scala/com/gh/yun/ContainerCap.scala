package com.gh.yun

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util

import com.gh.bean.alert.{AlertData, AlertDataInfo, KeyValue}
import com.gh.bean.containercapability.ContainerFileSystem
import com.gh.utils._
import org.apache.hadoop.fs.DF
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
object ContainerCap {
  def main(args: Array[String]) {
   /* if (args.length < 4) {
      System.err.println("Usage: Kafka <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }*/
    Logger.getRootLogger.setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("capability-container").setMaster(ConfigUtil.sparkmaster)
    val ssc = new StreamingContext(sparkConf, Seconds(ConfigUtil.capStreamtime))
    ssc.checkpoint(ConfigUtil.containerCapcheckpoint)

    var brokers = ConfigUtil.brokers
    val _topics = "capability-container".split(",").toSet
    val group = "capability-container-js"

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

    val datas = parseLog(stream)
    val container_group = datas.groupByKey()
    compute(container_group)

    ssc.start()
    ssc.awaitTermination()
  }

  def parseLog(stream : InputDStream[ConsumerRecord[String, String]]) : DStream[(String,(String,Double,Double,Double,Double,Double,Array[ContainerFileSystem]))] ={
    val datas = stream.map(line => {
          var node : JsonNode = JsonUtil.getJsonNode(line.value())
          node
    }).filter(x => (x != null && x.get("type") != null))
      .flatMap(node => {
            val _type = node.get("type").asText()

            val datas = node.get("data")
            val dataArr = ArrayBuffer[(String,(String,Double,Double,Double,Double,Double,Array[ContainerFileSystem]))]()
            for (i <- 0 to (datas.size() - 1) ){
              // 容器
              val data_i = node.get("data").get(i)
              val environment_id = data_i.get("environment_id").asText()
              val container_uuid = data_i.get("container_uuid").asText()

              var container_name = ""
              var namespace = ""
              try {
                container_name = data_i.get("container_name").asText()
                namespace = data_i.get("namespace").asText()
              }catch {
                case ex : Exception => {
                  println("--------> container_name/namespace is null")
                  ex.printStackTrace()
                }
              }

              val statsArr = ArrayBuffer[(String,(String,Double,Double,Double,Double,Double,Array[ContainerFileSystem]))]()

              if ("".equals(environment_id) || "".equals(container_uuid)) statsArr
              else {
                val _stats = data_i.get("stats")
                for (i <- 0 to (_stats.size() - 1) ){
                  val stats_i = _stats.get(i)
                  val timestamp = stats_i.get("timestamp").asText()

                  val cpu = stats_i.get("container_cpu_usage_seconds_total").asDouble()
                  val memory_usage = stats_i.get("container_memory_usage_bytes").asDouble()
                  val memory_limit = stats_i.get("container_memory_limit_bytes").asDouble()

                  val network_transmit = stats_i.get("container_network_transmit_bytes_total").asDouble()
                  val network_receive = stats_i.get("container_network_receive_bytes_total").asDouble()

                  val container_filesystem = stats_i.get("container_filesystem")
                  val diskNum = container_filesystem.size()
                  val disks =  new Array[ContainerFileSystem](diskNum)
                  for (i <- 0 to (diskNum - 1)){
                    val bean = JsonUtil.getBean(container_filesystem.get(i).toString,classOf[ContainerFileSystem])
                    disks(i) = bean
                  }
                  statsArr.+=( (environment_id+"#"+container_uuid+"#"+_type + "#" + container_name + "#" + namespace,(timestamp,cpu,memory_usage,memory_limit,network_transmit,network_receive,disks)) )
                }
              }
              dataArr.++=(statsArr)
            }
          dataArr
        })
    datas
  }

  def compute(container_group : DStream[(String,Iterable[(String,Double,Double,Double,Double,Double,Array[ContainerFileSystem])])]): Unit ={
      val warn = container_group.map(x => {
          val count = x._2.size

          val start_log = x._2.head
          val end_log = x._2.last
          val start_time = start_log._1.toString
          val end_time = end_log._1.toString
        // 1s = 1000 ms = 1000000 us = 1000000000 ns  => 1 ms = 1000000 ns
//                  println("passe:"+end_time+"------------start_time:"+start_time)
        //          val intervalInMs = df_utc_223231.parse(end_time).getTime() - df_utc_223231.parse(start_time).getTime() // 毫秒
          val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          var t1 = df.parse(start_time).getTime
          var t2 = df.parse(end_time).getTime
          val intervalInMs = t2 - t1 // 毫秒
          val intervalInSec = intervalInMs / 1000  // 秒
          val intervalNs = intervalInMs * 1000000  // 纳秒

          // cpu
          var cpu = (end_log._2 - start_log._2) / intervalNs
//          cpu = new BigDecimal(cpu).toString

          //memory
          var memory = - 1.0
          if (null != start_log._4 && 0 != start_log._4){
            val memory_sum = x._2.map(_._3).reduce(_+_)
            memory = memory_sum / count / start_log._4
          }

          // disk
          val _disks = end_log._7.map(x => (x.getContainer_filesystem_usage / x.getContainer_filesystem_capacity,x))

          // network_tx
          var network_tx = (end_log._5 - start_log._5) / intervalInSec     // byte/s
          network_tx = network_tx / 1024 / 1024

          // network_rx
          var network_rx = (end_log._6 - start_log._6) / intervalInSec     // byte/s
          network_rx = network_rx / 1024 / 1024

          (x._1,start_time,end_time,cpu,memory,_disks,network_tx,network_rx)
      }).map(aAlert(_))           // 条件判断
        .filter(_._4.size() > 0)  // 是否有告警信息

      // 超过阈值
      warn.foreachRDD( record => {
        val alerts = record.map(line => {
          AlertInfoUtil.toWarnBean(AlertInfoUtil.SUCCESS,AlertInfoUtil.ALERT_TYPE_M,AlertInfoUtil.ALERT_DIM_C,line._1, line._2, line._3, line._4,line._5)
        })
        if (!alerts.isEmpty()){
          val collect = alerts.collect()
          if (collect.size > 0) new HttpUtil().alerts(collect)  //告警
        }
      })

  }

  def aAlert(line : (String,String,String,Double,Double,Array[(Double,ContainerFileSystem)],Double,Double)): (String,String,String,util.ArrayList[KeyValue],String) ={
    var msg : String = ""
    val list = new util.ArrayList[KeyValue]()

   /* if (line._4 >= 0.9) list.add(new KeyValue("cpu",line._4.toString))
    if (-1 != line._5 && line._5 >= 0.7)  list.add(new KeyValue("memory",line._5.toString))
    val disks = line._6.filter(_._1 > 0.8)
    if (disks.size > 0 ) {
      list.add(new KeyValue("disk",disks(0)._1.toString))
      msg = disks.map(x => x._2.getContainer_filesystem_name + ",").toString.trim
      msg = msg.substring(0,msg.length - 2)
    }
    if (line._7 > 2) list.add(new KeyValue("network_tx",line._7.toString))
    if (line._8 > 2) list.add(new KeyValue("network_rx",line._8.toString))*/


    val cpu = HttpUtil.gz_map.get("container_cpu")
    cpu.getCondition match {
      case "GTE"  =>   if(line._4 >=  cpu.getValue)   list.add(new KeyValue("cpu",new BigDecimal(line._4).toString))
      case "GT"   =>   if(line._4  >  cpu.getValue)   list.add(new KeyValue("cpu",new BigDecimal(line._4).toString))
      case "LTE"  =>   if(line._4  <= cpu.getValue)   list.add(new KeyValue("cpu",new BigDecimal(line._4).toString))
      case "LT"   =>   if(line._4  <  cpu.getValue)   list.add(new KeyValue("cpu",new BigDecimal(line._4).toString))
      case "EQ"   =>   if(line._4  == cpu.getValue)   list.add(new KeyValue("cpu",new BigDecimal(line._4).toString))
    }

    val memory = HttpUtil.gz_map.get("container_memory")
    memory.getCondition match {
      case "GTE"  =>   if(-1 != line._5 && line._5  >= memory.getValue)   list.add(new KeyValue("memory",line._5.toString))
      case "GT"   =>   if(-1 != line._5 && line._5  >  memory.getValue)   list.add(new KeyValue("memory",line._5.toString))
      case "LTE"  =>   if(-1 != line._5 && line._5  <= memory.getValue)   list.add(new KeyValue("memory",line._5.toString))
      case "LT"   =>   if(-1 != line._5 && line._5  <  memory.getValue)   list.add(new KeyValue("memory",line._5.toString))
      case "EQ"   =>   if(-1 != line._5 && line._5  == memory.getValue)   list.add(new KeyValue("memory",line._5.toString))
    }


    val disk = HttpUtil.gz_map.get("container_disk")
    val disks = line._6.filter(x => {
        var flag = false
        disk.getCondition match {
          case "GTE"  =>   if(x._1 >=  disk.getValue)   flag = true
          case "GT"   =>   if(x._1  >  disk.getValue)   flag = true
          case "LTE"  =>   if(x._1  <= disk.getValue)   flag = true
          case "LT"   =>   if(x._1  <  disk.getValue)   flag = true
          case "EQ"   =>   if(x._1  == disk.getValue)   flag = true
        }
        flag
    })
    if (disks.size > 0 ) {
      list.add(new KeyValue("disk",disks(0)._1.toString))
      msg = disks.map(x => x._2.getContainer_filesystem_name + ",").toString.trim
      msg = msg.substring(0,msg.length - 2)
    }


    val network_tx = HttpUtil.gz_map.get("container_network_tx")
    network_tx.getCondition match {
      case "GTE"  =>   if(line._7  >= network_tx.getValue)   list.add(new KeyValue("network_tx",line._7.toString))
      case "GT"   =>   if(line._7  >  network_tx.getValue)   list.add(new KeyValue("network_tx",line._7.toString))
      case "LTE"  =>   if(line._7  <= network_tx.getValue)   list.add(new KeyValue("network_tx",line._7.toString))
      case "LT"   =>   if(line._7  <  network_tx.getValue)   list.add(new KeyValue("network_tx",line._7.toString))
      case "EQ"   =>   if(line._7  == network_tx.getValue)   list.add(new KeyValue("network_tx",line._7.toString))
    }

    val network_rx = HttpUtil.gz_map.get("container_network_rx")
    network_rx.getCondition match {
      case "GTE"  =>   if(line._8  >= network_rx.getValue)   list.add(new KeyValue("network_rx",line._8.toString))
      case "GT"   =>   if(line._8  >  network_rx.getValue)   list.add(new KeyValue("network_rx",line._8.toString))
      case "LTE"  =>   if(line._8  <= network_rx.getValue)   list.add(new KeyValue("network_rx",line._8.toString))
      case "LT"   =>   if(line._8  <  network_rx.getValue)   list.add(new KeyValue("network_rx",line._8.toString))
      case "EQ"   =>   if(line._8  == network_rx.getValue)   list.add(new KeyValue("network_rx",line._8.toString))
    }

    //test
//    list.add(new KeyValue("cpu",line._4.toString))
//    list.add(new KeyValue("network_tx",line._7.toString))

    (line._1,line._2,line._3,list,msg)
  }

}
