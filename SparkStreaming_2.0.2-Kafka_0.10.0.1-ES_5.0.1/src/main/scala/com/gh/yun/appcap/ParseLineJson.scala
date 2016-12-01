package com.gh.yun.appcap

import com.gh.utils.JsonUtil
import org.codehaus.jackson.JsonNode

/**
  * Created by GH-GAN on 2016/11/25.
  */
object ParseLineJson {
  def parse(s : String) : Unit ={
    val node: JsonNode = JsonUtil.getJsonNode(s)
    val app_cap_type = node.get("type").toString



    val string: JsonNode = node.get("data").get("stats").get(0).get("time_stamp")
    println(string)

  }

  def get(): Unit ={

  }

}
