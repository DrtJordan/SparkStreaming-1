package com.gh.yun

import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
/**
  * Created by GH-GAN on 2016/11/24.
  */
object ContainerCap {
  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local[2]").setAppName("es-test");
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", "192.168.100.154:8070,192.168.100.162:8070,192.168.100.163:8070,192.168.100.164:8070," +
      "192.168.100.166:8070,192.168.100.167:8070,192.168.100.168:8070,192.168.100.169:8070")

    val sc = new SparkContext(conf)

    val game = Map("media_type"->"game","title" -> "FF VI","year" -> "1994")
    val book = Map("media_type" -> "book","title" -> "Harry Potter","year" -> "2010")
    val cd = Map("media_type" -> "music","title" -> "Surfing With The Alien")

    val json = "{\"time_stamp\":\"2016-11-20T00:00:10.134Z\",\"data\":{\"log_info\":{\"log_time\":\"2016-11-20T09:00:10.134Z\",\"size\":396,\"app_file\":\"nginx_file1\"}}}"
    val json2 = "{\"time_stamp\":\"2016-11-20T00:00:10.134Z\",\"data\":{\"log_info\":{\"log_time\":\"2016-11-20T09:00:10.134Z\",\"size\":396,\"app_file\":\"nginx_file1\"}}}"


//    sc.makeRDD(Seq(game, book, cd)).saveToEs("my-collection-{media_type}/{media_type}")
    sc.makeRDD(Seq(json,json2)).saveJsonToEs("my-{data.log_info.app_file}/{time_stamp}")
  }
}
