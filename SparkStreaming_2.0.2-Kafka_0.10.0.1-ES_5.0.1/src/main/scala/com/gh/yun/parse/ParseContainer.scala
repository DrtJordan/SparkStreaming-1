package com.gh.yun.parse

import com.gh.bean.appcapability.{AppNginx, AppNginxData, AppNginxStats}
import com.gh.bean.containercapability._
import com.gh.utils.{DateUtil, JsonUtil}
import org.codehaus.jackson.JsonNode

import scala.collection.mutable.ArrayBuffer

/**
  * Created by GH-GAN on 2016/12/20.
  */
class ParseContainer {
  def statsToArray(_type: String,node : JsonNode): ArrayBuffer[String] ={
    val arr = ArrayBuffer[String]()
    try{
      var data = node.get("data")
      for (i <- 0 until data.size()){
        val data_i = data.get(i)

        val environment_id = data_i.get("environment_id").asText().trim
        val container_uuid = data_i.get("container_uuid").asText().trim
        val container_name = data_i.get("container_name").asText()
        val namespace = data_i.get("namespace").asText()
        val data_timestamp = data_i.get("timestamp").asText()

        if (!"".equals(environment_id) && !"".equals(container_uuid)){
          var stats = data_i.get("stats")
          for (j <- 0 until stats.size()){
            val stats_i = stats.get(j)
            val timestamp = stats_i.get("timestamp").asText()

            var cs = new ContainerStats()
            cs.setTimestamp(timestamp)
            cs.setContainer_cpu_usage_seconds_total(stats_i.get("container_cpu_usage_seconds_total").asDouble())
            cs.setContainer_cpu_user_seconds_total(stats_i.get("container_cpu_user_seconds_total").asDouble())
            cs.setContainer_cpu_system_seconds_total(stats_i.get("container_cpu_system_seconds_total").asDouble())
            cs.setContainer_memory_usage_bytes(stats_i.get("container_memory_usage_bytes").asDouble())
            cs.setContainer_memory_limit_bytes(stats_i.get("container_memory_limit_bytes").asDouble())
            cs.setContainer_memory_cache(stats_i.get("container_memory_cache").asDouble())
            cs.setContainer_memory_rss(stats_i.get("container_memory_rss").asDouble())
            cs.setContainer_memory_swap(stats_i.get("container_memory_swap").asDouble())
            cs.setContainer_network_receive_bytes_total(stats_i.get("container_network_receive_bytes_total").asDouble())
            cs.setContainer_network_receive_packets_total(stats_i.get("container_network_receive_packets_total").asDouble())
            cs.setContainer_network_receive_packets_dropped_total(stats_i.get("container_network_receive_packets_dropped_total").asDouble())
            cs.setContainer_network_receive_errors_total(stats_i.get("container_network_receive_errors_total").asDouble())
            cs.setContainer_network_transmit_bytes_total(stats_i.get("container_network_transmit_bytes_total").asDouble())
            cs.setContainer_network_transmit_packets_total(stats_i.get("container_network_transmit_packets_total").asDouble())
            cs.setContainer_network_transmit_packets_dropped_total(stats_i.get("container_network_transmit_packets_dropped_total").asDouble())
            cs.setContainer_network_transmit_errors_total(stats_i.get("container_network_transmit_errors_total").asDouble())

            val container_filesystem = stats_i.get("container_filesystem")
            val cfs = new Array[ContainerFileSystem](container_filesystem.size)

            for (z <- 0 until container_filesystem.size()){
              val container_filesystem_z = container_filesystem.get(z)
              var cfs_z = new ContainerFileSystem()
              cfs_z.setContainer_filesystem_name(container_filesystem_z.get("container_filesystem_name").asText())
              cfs_z.setContainer_filesystem_type(container_filesystem_z.get("container_filesystem_type").asText())
              cfs_z.setContainer_filesystem_capacity(container_filesystem_z.get("container_filesystem_capacity").asDouble())
              cfs_z.setContainer_filesystem_usage(container_filesystem_z.get("container_filesystem_usage").asDouble())
              cfs(z) = cfs_z
            }

            cs.setContainer_filesystem(cfs)
            cs.setContainer_diskio_service_bytes_async(stats_i.get("container_diskio_service_bytes_async").asDouble())
            cs.setContainer_diskio_service_bytes_read(stats_i.get("container_diskio_service_bytes_read").asDouble())
            cs.setContainer_diskio_service_bytes_sync(stats_i.get("container_diskio_service_bytes_sync").asDouble())
            cs.setContainer_diskio_service_bytes_total(stats_i.get("container_diskio_service_bytes_total").asDouble())
            cs.setContainer_diskio_service_bytes_write(stats_i.get("container_diskio_service_bytes_write").asDouble())
            cs.setContainer_tasks_state_nr_sleeping(stats_i.get("container_tasks_state_nr_sleeping").asDouble())
            cs.setContainer_tasks_state_nr_running(stats_i.get("container_tasks_state_nr_running").asDouble())
            cs.setContainer_tasks_state_nr_stopped(stats_i.get("container_tasks_state_nr_stopped").asDouble())
            cs.setContainer_tasks_state_nr_uninterruptible(stats_i.get("container_tasks_state_nr_uninterruptible").asDouble())
            cs.setContainer_tasks_state_nr_io_wait(stats_i.get("container_tasks_state_nr_io_wait").asDouble())

            var cd2 = new ContainerData2()
            cd2.setContainer_name(container_name)
            cd2.setContainer_uuid(container_uuid)
            cd2.setEnvironment_id(environment_id)
            cd2.setNamespace(namespace)
            cd2.setTimestamp(data_timestamp)
            cd2.setStats(cs)

            var container2 = new Container2()
            container2.setType(_type)
            container2.setData(cd2)

            // yun-{data.environment_id}-{data.container_uuid}-container-container-{data.stats.timestamp:YYYY-MM-dd}/container-cap
//            val index = "yun-" + environment_id + "-" + container_uuid + "-container-container-" + DateUtil.getYYYYMMdd(timestamp)
            arr.+=(JsonUtil.formatJson(container2))
          }
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
      for (i <- 0 until data.size()){
        val data_i = data.get(i)

        val environment_id = data_i.get("environment_id").asText().trim
        val container_uuid = data_i.get("container_uuid").asText().trim
        val container_name = data_i.get("container_name").asText()
        val namespace = data_i.get("namespace").asText()
        val data_timestamp = data_i.get("timestamp").asText()

        if (!"".equals(environment_id) && !"".equals(container_uuid)){
          var stats = data_i.get("stats")
          for (j <- 0 until stats.size()){
            val stats_i = stats.get(j)
            val timestamp = stats_i.get("timestamp").asText()

            var cs = new ContainerStats()
            cs.setTimestamp(timestamp)
            cs.setContainer_cpu_usage_seconds_total(stats_i.get("container_cpu_usage_seconds_total").asDouble())
            cs.setContainer_cpu_user_seconds_total(stats_i.get("container_cpu_user_seconds_total").asDouble())
            cs.setContainer_cpu_system_seconds_total(stats_i.get("container_cpu_system_seconds_total").asDouble())
            cs.setContainer_memory_usage_bytes(stats_i.get("container_memory_usage_bytes").asDouble())
            cs.setContainer_memory_limit_bytes(stats_i.get("container_memory_limit_bytes").asDouble())
            cs.setContainer_memory_cache(stats_i.get("container_memory_cache").asDouble())
            cs.setContainer_memory_rss(stats_i.get("container_memory_rss").asDouble())
            cs.setContainer_memory_swap(stats_i.get("container_memory_swap").asDouble())
            cs.setContainer_network_receive_bytes_total(stats_i.get("container_network_receive_bytes_total").asDouble())
            cs.setContainer_network_receive_packets_total(stats_i.get("container_network_receive_packets_total").asDouble())
            cs.setContainer_network_receive_packets_dropped_total(stats_i.get("container_network_receive_packets_dropped_total").asDouble())
            cs.setContainer_network_receive_errors_total(stats_i.get("container_network_receive_errors_total").asDouble())
            cs.setContainer_network_transmit_bytes_total(stats_i.get("container_network_transmit_bytes_total").asDouble())
            cs.setContainer_network_transmit_packets_total(stats_i.get("container_network_transmit_packets_total").asDouble())
            cs.setContainer_network_transmit_packets_dropped_total(stats_i.get("container_network_transmit_packets_dropped_total").asDouble())
            cs.setContainer_network_transmit_errors_total(stats_i.get("container_network_transmit_errors_total").asDouble())

            val container_filesystem = stats_i.get("container_filesystem")
            val cfs = new Array[ContainerFileSystem](container_filesystem.size)

            for (z <- 0 until container_filesystem.size()){
              val container_filesystem_z = container_filesystem.get(z)
              var cfs_z = new ContainerFileSystem()
              cfs_z.setContainer_filesystem_name(container_filesystem_z.get("container_filesystem_name").asText())
              cfs_z.setContainer_filesystem_type(container_filesystem_z.get("container_filesystem_type").asText())
              cfs_z.setContainer_filesystem_capacity(container_filesystem_z.get("container_filesystem_capacity").asDouble())
              cfs_z.setContainer_filesystem_usage(container_filesystem_z.get("container_filesystem_usage").asDouble())
              cfs(z) = cfs_z
            }

            cs.setContainer_filesystem(cfs)
            cs.setContainer_diskio_service_bytes_async(stats_i.get("container_diskio_service_bytes_async").asDouble())
            cs.setContainer_diskio_service_bytes_read(stats_i.get("container_diskio_service_bytes_read").asDouble())
            cs.setContainer_diskio_service_bytes_sync(stats_i.get("container_diskio_service_bytes_sync").asDouble())
            cs.setContainer_diskio_service_bytes_total(stats_i.get("container_diskio_service_bytes_total").asDouble())
            cs.setContainer_diskio_service_bytes_write(stats_i.get("container_diskio_service_bytes_write").asDouble())
            cs.setContainer_tasks_state_nr_sleeping(stats_i.get("container_tasks_state_nr_sleeping").asDouble())
            cs.setContainer_tasks_state_nr_running(stats_i.get("container_tasks_state_nr_running").asDouble())
            cs.setContainer_tasks_state_nr_stopped(stats_i.get("container_tasks_state_nr_stopped").asDouble())
            cs.setContainer_tasks_state_nr_uninterruptible(stats_i.get("container_tasks_state_nr_uninterruptible").asDouble())
            cs.setContainer_tasks_state_nr_io_wait(stats_i.get("container_tasks_state_nr_io_wait").asDouble())

            var cd2 = new ContainerData2()
            cd2.setContainer_name(container_name)
            cd2.setContainer_uuid(container_uuid)
            cd2.setEnvironment_id(environment_id)
            cd2.setNamespace(namespace)
            cd2.setTimestamp(data_timestamp)
            cd2.setStats(cs)

            var container2 = new Container2()
            container2.setType(_type)
            container2.setData(cd2)

            // yun-{data.environment_id}-{data.container_uuid}-container-container-{data.stats.timestamp:YYYY-MM-dd}/container-cap
            val index = "yun-" + environment_id + "-" + container_uuid + "-container-container-" + DateUtil.getYYYYMMdd(timestamp)
            arr.+=((index,JsonUtil.formatJson(container2)))
          }
        }
      }
    }catch {
      case ex : Exception => ex.printStackTrace()
    }
    arr
  }
}
