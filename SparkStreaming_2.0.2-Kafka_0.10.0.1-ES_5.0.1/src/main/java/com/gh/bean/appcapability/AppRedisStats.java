package com.gh.bean.appcapability;

/**
 * Created by GH-GAN on 2016/11/29.
 */
public class AppRedisStats {
    String timestamp;
    String uptime_in_seconds;
    String connected_clients;
    String blocked_clients;
    String used_memory;
    String used_memory_rss;
    String used_memory_peak;
    String used_memory_lua;
    String max_memory;
    String mem_fragmentation_ratio;
    String rdb_changes_since_last_save;
    String rdb_last_bgsave_time_sec;
    String rdb_current_bgsave_time_sec;
    String aof_enabled;
    String aof_rewrite_in_progress;
    String aof_rewrite_scheduled;
    String aof_last_rewrite_time_sec;
    String aof_current_rewrite_time_sec;
    String total_connections_received;
    String total_commands_processed;
    String total_net_input_bytes;
    String total_net_output_bytes;
    String rejected_connections;
    String expired_keys;
    String evicted_keys;
    String keyspace_hits;
    String keyspace_misses;
    String pubsub_channels;
    String pubsub_patterns;
    String loading;
    String connected_slaves;
    String repl_backlog_size;
    String used_cpu_sys;
    String used_cpu_user;
    String used_cpu_sys_children;
    String used_cpu_user_children;

   
}
