package com.gh.yun.parse

import com.gh.bean.appcapability._
import com.gh.utils.{DateUtil, JsonUtil}
import org.codehaus.jackson.JsonNode

import scala.collection.mutable.ArrayBuffer

/**
  * Created by GH-GAN on 2016/12/20.
  */
class ParseRedis {
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

          val ars = new AppRedisStats()
          val timestamp = stats_i.get("timestamp").asText()
          ars.setTimestamp(timestamp)
          ars.setUptime_in_seconds(stats_i.get("uptime_in_seconds").asDouble())
          ars.setConnected_clients(stats_i.get("connected_clients").asDouble())
          ars.setBlocked_clients(stats_i.get("blocked_clients").asDouble())
          ars.setUsed_memory(stats_i.get("used_memory").asDouble())
          ars.setUsed_memory_rss(stats_i.get("used_memory_rss").asDouble())
          ars.setUsed_memory_peak(stats_i.get("used_memory_peak").asDouble())
          ars.setUsed_memory_lua(stats_i.get("used_memory_lua").asDouble())
          ars.setMax_memory(stats_i.get("max_memory").asDouble())
          ars.setMem_fragmentation_ratio(stats_i.get("mem_fragmentation_ratio").asDouble())
          ars.setRdb_changes_since_last_save(stats_i.get("rdb_changes_since_last_save").asDouble())
          ars.setRdb_last_bgsave_time_sec(stats_i.get("rdb_last_bgsave_time_sec").asDouble())
          ars.setRdb_current_bgsave_time_sec(stats_i.get("rdb_current_bgsave_time_sec").asDouble())
          ars.setAof_enabled(stats_i.get("aof_enabled").asDouble())
          ars.setAof_rewrite_in_progress(stats_i.get("aof_rewrite_in_progress").asDouble())
          ars.setAof_rewrite_scheduled(stats_i.get("aof_rewrite_scheduled").asDouble())
          ars.setAof_last_rewrite_time_sec(stats_i.get("aof_last_rewrite_time_sec").asDouble())
          ars.setAof_current_rewrite_time_sec(stats_i.get("aof_current_rewrite_time_sec").asDouble())
          ars.setTotal_connections_received(stats_i.get("total_connections_received").asDouble())
          ars.setTotal_commands_processed(stats_i.get("total_commands_processed").asDouble())
          ars.setTotal_net_input_bytes(stats_i.get("total_net_input_bytes").asDouble())
          ars.setTotal_net_output_bytes(stats_i.get("total_net_output_bytes").asDouble())
          ars.setRejected_connections(stats_i.get("rejected_connections").asDouble())
          ars.setExpired_keys(stats_i.get("expired_keys").asDouble())
          ars.setEvicted_keys(stats_i.get("evicted_keys").asDouble())
          ars.setKeyspace_hits(stats_i.get("keyspace_hits").asDouble())
          ars.setKeyspace_misses(stats_i.get("keyspace_misses").asDouble())
          ars.setPubsub_channels(stats_i.get("pubsub_channels").asDouble())
          ars.setPubsub_patterns(stats_i.get("pubsub_patterns").asDouble())
          ars.setLoading(stats_i.get("loading").asDouble())
          ars.setConnected_slaves(stats_i.get("connected_slaves").asDouble())
          ars.setRepl_backlog_size(stats_i.get("repl_backlog_size").asDouble())
          ars.setUsed_cpu_sys(stats_i.get("used_cpu_sys").asDouble())
          ars.setUsed_cpu_user(stats_i.get("used_cpu_user").asDouble())
          ars.setUsed_cpu_sys_children(stats_i.get("used_cpu_sys_children").asDouble())
          ars.setUsed_cpu_user_children(stats_i.get("used_cpu_user_children").asDouble())

          var ard2 = new AppRedisData2()
          ard2.setContainer_name(container_name)
          ard2.setContainer_uuid(container_uuid)
          ard2.setEnvironment_id(environment_id)
          ard2.setNamespace(namespace)
          ard2.setTimestamp(data_timestamp)
          ard2.setStats(ars)

          var ar2 = new AppRedis2()
          ar2.setType(_type)
          ar2.setData(ard2)

          // index  yun-{data.environment_id}-{data.container_uuid}-app-redis-{data.stats.timestamp:YYYY-MM-dd}
//          val index = "yun-" + environment_id + "-" + container_uuid + "-app-redis-" + DateUtil.getYYYYMMdd(timestamp)
          arr.+=(JsonUtil.formatJson(ar2))
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

          val ars = new AppRedisStats()
          val timestamp = stats_i.get("timestamp").asText()
          ars.setTimestamp(timestamp)
          ars.setUptime_in_seconds(stats_i.get("uptime_in_seconds").asDouble())
          ars.setConnected_clients(stats_i.get("connected_clients").asDouble())
          ars.setBlocked_clients(stats_i.get("blocked_clients").asDouble())
          ars.setUsed_memory(stats_i.get("used_memory").asDouble())
          ars.setUsed_memory_rss(stats_i.get("used_memory_rss").asDouble())
          ars.setUsed_memory_peak(stats_i.get("used_memory_peak").asDouble())
          ars.setUsed_memory_lua(stats_i.get("used_memory_lua").asDouble())
          ars.setMax_memory(stats_i.get("max_memory").asDouble())
          ars.setMem_fragmentation_ratio(stats_i.get("mem_fragmentation_ratio").asDouble())
          ars.setRdb_changes_since_last_save(stats_i.get("rdb_changes_since_last_save").asDouble())
          ars.setRdb_last_bgsave_time_sec(stats_i.get("rdb_last_bgsave_time_sec").asDouble())
          ars.setRdb_current_bgsave_time_sec(stats_i.get("rdb_current_bgsave_time_sec").asDouble())
          ars.setAof_enabled(stats_i.get("aof_enabled").asDouble())
          ars.setAof_rewrite_in_progress(stats_i.get("aof_rewrite_in_progress").asDouble())
          ars.setAof_rewrite_scheduled(stats_i.get("aof_rewrite_scheduled").asDouble())
          ars.setAof_last_rewrite_time_sec(stats_i.get("aof_last_rewrite_time_sec").asDouble())
          ars.setAof_current_rewrite_time_sec(stats_i.get("aof_current_rewrite_time_sec").asDouble())
          ars.setTotal_connections_received(stats_i.get("total_connections_received").asDouble())
          ars.setTotal_commands_processed(stats_i.get("total_commands_processed").asDouble())
          ars.setTotal_net_input_bytes(stats_i.get("total_net_input_bytes").asDouble())
          ars.setTotal_net_output_bytes(stats_i.get("total_net_output_bytes").asDouble())
          ars.setRejected_connections(stats_i.get("rejected_connections").asDouble())
          ars.setExpired_keys(stats_i.get("expired_keys").asDouble())
          ars.setEvicted_keys(stats_i.get("evicted_keys").asDouble())
          ars.setKeyspace_hits(stats_i.get("keyspace_hits").asDouble())
          ars.setKeyspace_misses(stats_i.get("keyspace_misses").asDouble())
          ars.setPubsub_channels(stats_i.get("pubsub_channels").asDouble())
          ars.setPubsub_patterns(stats_i.get("pubsub_patterns").asDouble())
          ars.setLoading(stats_i.get("loading").asDouble())
          ars.setConnected_slaves(stats_i.get("connected_slaves").asDouble())
          ars.setRepl_backlog_size(stats_i.get("repl_backlog_size").asDouble())
          ars.setUsed_cpu_sys(stats_i.get("used_cpu_sys").asDouble())
          ars.setUsed_cpu_user(stats_i.get("used_cpu_user").asDouble())
          ars.setUsed_cpu_sys_children(stats_i.get("used_cpu_sys_children").asDouble())
          ars.setUsed_cpu_user_children(stats_i.get("used_cpu_user_children").asDouble())

          var ard2 = new AppRedisData2()
          ard2.setContainer_name(container_name)
          ard2.setContainer_uuid(container_uuid)
          ard2.setEnvironment_id(environment_id)
          ard2.setNamespace(namespace)
          ard2.setTimestamp(data_timestamp)
          ard2.setStats(ars)

          var ar2 = new AppRedis2()
          ar2.setType(_type)
          ar2.setData(ard2)

          // index  yun-{data.environment_id}-{data.container_uuid}-app-redis-{data.stats.timestamp:YYYY-MM-dd}
          val index = "yun-" + environment_id + "-" + container_uuid + "-app-redis-" + DateUtil.getYYYYMMdd(timestamp)
          arr.+=((index, JsonUtil.formatJson(ar2)))
        }
      }
    }catch {
      case ex : Exception => ex.printStackTrace()
    }
    arr
  }

}
