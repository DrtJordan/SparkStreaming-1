package org.hna.stream

import java.net.URLDecoder
import org.hna.dao.MysqlDao
import org.hna.utils.Config
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils

import scala.util.parsing.json._

object KafkaStreaming {
  val mysql = new MysqlDao()
  def main(args: Array[String]) {
    val sparkconf = new SparkConf().setAppName("kafka").setMaster(Config.master)
    val sc = new SparkContext(sparkconf)
    val ssc = new StreamingContext(sc,Seconds(5))
    ssc.checkpoint(Config.checkpoint)

    val zkList = Config.zklist
    val groupId = "test-consumer-group"
    val topicMap = Config.kafkatopic.split(",").map(x => (x,2)).toMap
    val lines = KafkaUtils.createStream(ssc,zkList,groupId,topicMap).map(_._2)
    parseAndSave(lines)
    //lines.print()
    ssc.start()
    ssc.awaitTermination()
  }

  def parseAndSave(lines : DStream[String]) = {
    lines.foreachRDD(rdd => {
      rdd.foreach(f = record => {
        var line = record.toString
        //println("line=>"+line)
        try {
          //解码两次
          line = URLDecoder.decode(line, "utf-8")
          line = URLDecoder.decode(line, "utf-8")

          val json = JSON.parseFull(line)
          json match {
            case Some(map: Map[String, Any]) =>
              val sql = getSql(map)
              mysql.save(sql)
            case None => println("json parse error !!! => None")
          }
        } catch {
          case e: Exception => e.printStackTrace()
            println("json parse error !!! ")
        }
      })
    })
  }
  def getSql(map:Map[String,Any]) : String ={
    val sql = new StringBuffer()
    sql.append("INSERT INTO request_log (website,time_local,remote_addr,remote_port,request,request_body,response_time,request_time,status,body_bytes_sent,http_referer,http_user_agent,headers,jsession,http_cookie,resp_body)")
    sql.append("VALUES")
      sql.append("(")
      sql.append("\""+map.get("website").get.toString+"\"")
      sql.append(",\""+map.get("time_local").get.toString+"\"")
      sql.append(",\""+map.get("remote_addr").get.toString+"\"")
      sql.append(",\""+map.get("remote_port").get.toString+"\"")
      sql.append(",\""+map.get("request").get.toString+"\"")
      sql.append(",\""+map.get("request_body").get.toString+"\"")
      sql.append(",\""+map.get("response_time").get.toString+"\"")
      sql.append(",\""+map.get("request_time").get.toString+"\"")
      sql.append(",\""+map.get("status").get.toString+"\"")
      sql.append(",\""+map.get("body_bytes_sent").get.toString+"\"")
      sql.append(",\""+map.get("http_referer").get.toString+"\"")
      sql.append(",\""+map.get("http_user_agent").get.toString+"\"")
      sql.append(",\""+map.get("headers").get.toString+"\"")
      sql.append(",\""+map.get("jsession").get.toString +"\"")
      sql.append(",\""+ map.get("http_cookie").get.toString +"\"")
      sql.append(",\""+map.get("resp_body").get.toString+"\"")
      sql.append(")")

    sql.toString
  }
}
