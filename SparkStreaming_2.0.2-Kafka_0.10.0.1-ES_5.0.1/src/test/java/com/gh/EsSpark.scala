package com.gh

/**
  * Created by GH-GAN on 2016/6/3.
  */

import org.apache.spark.{SparkConf, SparkContext}
//import org.elasticsearch.spark._
object EsSpark {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local").setAppName("es-test-11");
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes", "192.168.100.154:8070,192.168.100.162:8070,192.168.100.163:8070,192.168.100.164:8070,192.168.100.166:8070,192.168.100.167:8070,192.168.100.168:8070,192.168.100.169:8070")

    val sc = new SparkContext(conf)


    // 字段
//    df.columns.foreach(col => println(col))


    // 数据插入es
    val numbers = Map("one" -> 1, "two" -> 2, "three" -> 3)
    val airports = Map("arrival" -> "Otopeni", "SFO" -> "San Fran")
//    sc.makeRDD(Seq(numbers, airports)).saveToEs("es-test-13/docs")

//    sc.stop()

  }
}
