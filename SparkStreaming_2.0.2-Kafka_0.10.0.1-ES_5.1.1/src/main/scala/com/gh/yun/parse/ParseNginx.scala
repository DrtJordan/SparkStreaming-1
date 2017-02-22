package com.gh.yun.parse

import com.gh.bean.appcapability.{AppNginx, AppNginxData, AppNginxStats}
import com.gh.utils.{DateUtil, JsonUtil}
import org.codehaus.jackson.JsonNode

import scala.collection.mutable.ArrayBuffer

/**
  * Created by GH-GAN on 2016/12/20.
  */
class ParseNginx {
  def statsToArray(_type: String,node : JsonNode): ArrayBuffer[String] ={
    val arr = ArrayBuffer[String]()
    try{
        var data = node.get("data")
        val environment_id = data.get("environment_id").asText()
        val container_uuid = data.get("container_uuid").asText()
        val container_name = data.get("container_name").asText()
        val namespace = data.get("namespace").asText()
        val data_timestamp = data.get("timestamp").asText()

        if (!"".equals(environment_id) && !"".equals(container_uuid)){
          var _stats = data.get("stats")
          for (i <- 0 to (_stats.size() - 1) ){
            val stats = _stats.get(i)
            val timestamp = stats.get("timestamp").asText()
            val active_connections = stats.get("active_connections").asDouble()
            val accepts = stats.get("accepts").asDouble()
            val handled = stats.get("handled").asDouble()
            val requests = stats.get("requests").asDouble()
            val reading = stats.get("reading").asDouble()
            val writing = stats.get("writing").asDouble()
            val waiting = stats.get("waiting").asDouble()

            val  _ng_stats = new AppNginxStats()
            _ng_stats.setTimestamp(timestamp)
            _ng_stats.setActive_connections(active_connections)
            _ng_stats.setAccepts(accepts)
            _ng_stats.setHandled(handled)
            _ng_stats.setRequests(requests)
            _ng_stats.setReading(reading)
            _ng_stats.setWiting(writing)
            _ng_stats.setWaiting(waiting)

            val _ng_data = new AppNginxData()
            _ng_data.setEnvironment_id(environment_id)
            _ng_data.setContainer_uuid(container_uuid)
            _ng_data.setContainer_name(container_name)
            _ng_data.setNamespace(namespace)
            _ng_data.setTimestamp(data_timestamp)
            _ng_data.setStats(_ng_stats)

            val _ng = new AppNginx()
            _ng.setType(_type)
            _ng.setData(_ng_data)

            arr.+=(JsonUtil.formatJson(_ng))
          }
        }
    }catch {
      case ex : Exception => ex.printStackTrace()
    }
    arr
  }

  def statsToArray2(_type: String,node : JsonNode): ArrayBuffer[(String,String)] ={
    val arr = ArrayBuffer[(String,String)]()
    try{
      var data = node.get("data")
      val environment_id = data.get("environment_id").asText().trim
      val container_uuid = data.get("container_uuid").asText().trim
      val container_name = data.get("container_name").asText()
      val namespace = data.get("namespace").asText()
      val data_timestamp = data.get("timestamp").asText()

      if (!"".equals(environment_id) && !"".equals(container_uuid)){
        var _stats = data.get("stats")
        for (i <- 0 until _stats.size()){
          val stats = _stats.get(i)
          val timestamp = stats.get("timestamp").asText()
          val active_connections = stats.get("active_connections").asDouble()
          val accepts = stats.get("accepts").asDouble()
          val handled = stats.get("handled").asDouble()
          val requests = stats.get("requests").asDouble()
          val reading = stats.get("reading").asDouble()
          val writing = stats.get("writing").asDouble()
          val waiting = stats.get("waiting").asDouble()

          val  _ng_stats = new AppNginxStats()
          _ng_stats.setTimestamp(timestamp)
          _ng_stats.setActive_connections(active_connections)
          _ng_stats.setAccepts(accepts)
          _ng_stats.setHandled(handled)
          _ng_stats.setRequests(requests)
          _ng_stats.setReading(reading)
          _ng_stats.setWiting(writing)
          _ng_stats.setWaiting(waiting)

          val _ng_data = new AppNginxData()
          _ng_data.setEnvironment_id(environment_id)
          _ng_data.setContainer_uuid(container_uuid)
          _ng_data.setContainer_name(container_name)
          _ng_data.setNamespace(namespace)
          _ng_data.setTimestamp(data_timestamp)
          _ng_data.setStats(_ng_stats)

          val _ng = new AppNginx()
          _ng.setType(_type)
          _ng.setData(_ng_data)

          // index  yun-{data.environment_id}-{data.container_uuid}-app-nginx-{data.stats.timestamp:YYYY-MM-dd}
          val index = "yun-" + environment_id + "-" + container_uuid + "-app-nginx-" + DateUtil.getYYYYMMdd(timestamp)
          arr.+=((index,JsonUtil.formatJson(_ng)))
        }
      }
    }catch {
      case ex : Exception => ex.printStackTrace()
    }
    arr
  }

}
