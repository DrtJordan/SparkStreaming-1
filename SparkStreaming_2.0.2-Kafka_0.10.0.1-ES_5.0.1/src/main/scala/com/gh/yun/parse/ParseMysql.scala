package com.gh.yun.parse

import com.gh.bean.appcapability._
import com.gh.utils.{DateUtil, JsonUtil}
import org.codehaus.jackson.JsonNode

import scala.collection.mutable.ArrayBuffer

/**
  * Created by GH-GAN on 2016/12/20.
  */
class ParseMysql {
  def statsToArray(_type: String,node : JsonNode): ArrayBuffer[String] ={
    val arr = ArrayBuffer[String]()
    try{
      var data = node.get("data")
      val environment_id = data.get("environment_id").asText().trim
      val container_uuid = data.get("container_uuid").asText().trim
      val container_name = data.get("container_name").asText()
      val namespace = data.get("namespace").asText()
      val data_timestamp = data.get("timestamp").asText()

      if (!"".equals(environment_id) && !"".equals(container_uuid)){
        var _stats = data.get("stats")
        for (i <- 0 until _stats.size()) {
          val stats_i = _stats.get(i)

          val ams = new AppMysqlStats()
          val timestamp = stats_i.get("timestamp").asText()
          ams.setTimestamp(timestamp)
          ams.setConnections_total(stats_i.get("connections_total").asDouble())
          ams.setCommand_query_total(stats_i.get("command_query_total").asDouble())
          ams.setCommand_insert_total(stats_i.get("command_insert_total").asDouble())
          ams.setCommand_update_total(stats_i.get("command_update_total").asDouble())
          ams.setCommand_delete_total(stats_i.get("command_delete_total").asDouble())
          ams.setCommands_total(stats_i.get("commands_total").asDouble())
          ams.setHandlers_total(stats_i.get("handlers_total").asDouble())
          ams.setConnection_errors_total(stats_i.get("connection_errors_total").asDouble())
          ams.setBuffer_pool_pages(stats_i.get("buffer_pool_pages").asDouble())
          ams.setThread_connected(stats_i.get("thread_connected").asDouble())
          ams.setMax_connections(stats_i.get("max_connections").asDouble())
          ams.setQuery_response_time_seconds(stats_i.get("query_response_time_seconds").asDouble())
          ams.setRead_query_response_time_seconds(stats_i.get("read_query_response_time_seconds").asDouble())
          ams.setWrite_query_response_time_seconds(stats_i.get("write_query_response_time_seconds").asDouble())
          ams.setQueries_inside_innodb(stats_i.get("queries_inside_innodb").asDouble())
          ams.setQueries_in_queue(stats_i.get("queries_in_queue").asDouble())
          ams.setRead_views_open_inside_innodb(stats_i.get("read_views_open_inside_innodb").asDouble())
          ams.setTable_statistics_rows_read_total(stats_i.get("table_statistics_rows_read_total").asDouble())
          ams.setTable_statistics_rows_changed_total(stats_i.get("table_statistics_rows_changed_total").asDouble())
          ams.setTable_statistics_rows_changed_x_indexes_total(stats_i.get("table_statistics_rows_changed_x_indexes_total").asDouble())
          ams.setSql_lock_waits_total(stats_i.get("sql_lock_waits_total").asDouble())
          ams.setExternal_lock_waits_total(stats_i.get("external_lock_waits_total").asDouble())
          ams.setSql_lock_waits_seconds_total(stats_i.get("sql_lock_waits_seconds_total").asDouble())
          ams.setExternal_lock_waits_seconds_total(stats_i.get("external_lock_waits_seconds_total").asDouble())
          ams.setTable_io_waits_total(stats_i.get("table_io_waits_total").asDouble())
          ams.setTable_io_waits_seconds_total(stats_i.get("table_io_waits_seconds_total").asDouble())

          var amsd = new AppMySqlData2()
          amsd.setContainer_name(container_name)
          amsd.setContainer_uuid(container_uuid)
          amsd.setEnvironment_id(environment_id)
          amsd.setNamespace(namespace)
          amsd.setTimestamp(data_timestamp)
          amsd.setStats(ams)

          var ms = new AppMySql2()
          ms.setType(_type)
          ms.setData(amsd)

          // index  yun-{data.environment_id}-{data.container_uuid}-app-mysql-{data.stats.timestamp:YYYY-MM-dd}
//          val index = "yun-" + environment_id + "-" + container_uuid + "-app-mysql-" + DateUtil.getYYYYMMdd(timestamp)
          arr.+=(JsonUtil.formatJson(ms))
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
        for (i <- 0 until _stats.size()) {
          val stats_i = _stats.get(i)

          val ams = new AppMysqlStats()
          val timestamp = stats_i.get("timestamp").asText()
          ams.setTimestamp(timestamp)
          ams.setConnections_total(stats_i.get("connections_total").asDouble())
          ams.setCommand_query_total(stats_i.get("command_query_total").asDouble())
          ams.setCommand_insert_total(stats_i.get("command_insert_total").asDouble())
          ams.setCommand_update_total(stats_i.get("command_update_total").asDouble())
          ams.setCommand_delete_total(stats_i.get("command_delete_total").asDouble())
          ams.setCommands_total(stats_i.get("commands_total").asDouble())
          ams.setHandlers_total(stats_i.get("handlers_total").asDouble())
          ams.setConnection_errors_total(stats_i.get("connection_errors_total").asDouble())
          ams.setBuffer_pool_pages(stats_i.get("buffer_pool_pages").asDouble())
          ams.setThread_connected(stats_i.get("thread_connected").asDouble())
          ams.setMax_connections(stats_i.get("max_connections").asDouble())
          ams.setQuery_response_time_seconds(stats_i.get("query_response_time_seconds").asDouble())
          ams.setRead_query_response_time_seconds(stats_i.get("read_query_response_time_seconds").asDouble())
          ams.setWrite_query_response_time_seconds(stats_i.get("write_query_response_time_seconds").asDouble())
          ams.setQueries_inside_innodb(stats_i.get("queries_inside_innodb").asDouble())
          ams.setQueries_in_queue(stats_i.get("queries_in_queue").asDouble())
          ams.setRead_views_open_inside_innodb(stats_i.get("read_views_open_inside_innodb").asDouble())
          ams.setTable_statistics_rows_read_total(stats_i.get("table_statistics_rows_read_total").asDouble())
          ams.setTable_statistics_rows_changed_total(stats_i.get("table_statistics_rows_changed_total").asDouble())
          ams.setTable_statistics_rows_changed_x_indexes_total(stats_i.get("table_statistics_rows_changed_x_indexes_total").asDouble())
          ams.setSql_lock_waits_total(stats_i.get("sql_lock_waits_total").asDouble())
          ams.setExternal_lock_waits_total(stats_i.get("external_lock_waits_total").asDouble())
          ams.setSql_lock_waits_seconds_total(stats_i.get("sql_lock_waits_seconds_total").asDouble())
          ams.setExternal_lock_waits_seconds_total(stats_i.get("external_lock_waits_seconds_total").asDouble())
          ams.setTable_io_waits_total(stats_i.get("table_io_waits_total").asDouble())
          ams.setTable_io_waits_seconds_total(stats_i.get("table_io_waits_seconds_total").asDouble())

          var amsd = new AppMySqlData2()
          amsd.setContainer_name(container_name)
          amsd.setContainer_uuid(container_uuid)
          amsd.setEnvironment_id(environment_id)
          amsd.setNamespace(namespace)
          amsd.setTimestamp(data_timestamp)
          amsd.setStats(ams)

          var ms = new AppMySql2()
          ms.setType(_type)
          ms.setData(amsd)

          // index  yun-{data.environment_id}-{data.container_uuid}-app-mysql-{data.stats.timestamp:YYYY-MM-dd}
          val index = "yun-" + environment_id + "-" + container_uuid + "-app-mysql-" + DateUtil.getYYYYMMdd(timestamp)
          arr.+=((index, JsonUtil.formatJson(ms)))
        }
      }
    }catch {
      case ex : Exception => ex.printStackTrace()
    }
    arr
  }

}
