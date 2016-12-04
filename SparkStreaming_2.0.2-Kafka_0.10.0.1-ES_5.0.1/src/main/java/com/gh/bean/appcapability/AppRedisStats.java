package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/29.
 */
public class AppRedisStats  implements Serializable {
    String timestamp;
    Double uptime_in_seconds;
    Double connected_clients;
    Double blocked_clients;
    Double used_memory;
    Double used_memory_rss;
    Double used_memory_peak;
    Double used_memory_lua;
    Double max_memory;
    Double mem_fragmentation_ratio;
    Double rdb_changes_since_last_save;
    Double rdb_last_bgsave_time_sec;
    Double rdb_current_bgsave_time_sec;
    Double aof_enabled;
    Double aof_rewrite_in_progress;
    Double aof_rewrite_scheduled;
    Double aof_last_rewrite_time_sec;
    Double aof_current_rewrite_time_sec;
    Double total_connections_received;
    Double total_commands_processed;
    Double total_net_input_bytes;
    Double total_net_output_bytes;
    Double rejected_connections;
    Double expired_keys;
    Double evicted_keys;
    Double keyspace_hits;
    Double keyspace_misses;
    Double pubsub_channels;
    Double pubsub_patterns;
    Double loading;
    Double connected_slaves;
    Double repl_backlog_size;
    Double used_cpu_sys;
    Double used_cpu_user;
    Double used_cpu_sys_children;
    Double used_cpu_user_children;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getUptime_in_seconds() {
        return uptime_in_seconds;
    }

    public void setUptime_in_seconds(Double uptime_in_seconds) {
        this.uptime_in_seconds = uptime_in_seconds;
    }

    public Double getConnected_clients() {
        return connected_clients;
    }

    public void setConnected_clients(Double connected_clients) {
        this.connected_clients = connected_clients;
    }

    public Double getBlocked_clients() {
        return blocked_clients;
    }

    public void setBlocked_clients(Double blocked_clients) {
        this.blocked_clients = blocked_clients;
    }

    public Double getUsed_memory() {
        return used_memory;
    }

    public void setUsed_memory(Double used_memory) {
        this.used_memory = used_memory;
    }

    public Double getUsed_memory_rss() {
        return used_memory_rss;
    }

    public void setUsed_memory_rss(Double used_memory_rss) {
        this.used_memory_rss = used_memory_rss;
    }

    public Double getUsed_memory_peak() {
        return used_memory_peak;
    }

    public void setUsed_memory_peak(Double used_memory_peak) {
        this.used_memory_peak = used_memory_peak;
    }

    public Double getUsed_memory_lua() {
        return used_memory_lua;
    }

    public void setUsed_memory_lua(Double used_memory_lua) {
        this.used_memory_lua = used_memory_lua;
    }

    public Double getMax_memory() {
        return max_memory;
    }

    public void setMax_memory(Double max_memory) {
        this.max_memory = max_memory;
    }

    public Double getMem_fragmentation_ratio() {
        return mem_fragmentation_ratio;
    }

    public void setMem_fragmentation_ratio(Double mem_fragmentation_ratio) {
        this.mem_fragmentation_ratio = mem_fragmentation_ratio;
    }

    public Double getRdb_changes_since_last_save() {
        return rdb_changes_since_last_save;
    }

    public void setRdb_changes_since_last_save(Double rdb_changes_since_last_save) {
        this.rdb_changes_since_last_save = rdb_changes_since_last_save;
    }

    public Double getRdb_last_bgsave_time_sec() {
        return rdb_last_bgsave_time_sec;
    }

    public void setRdb_last_bgsave_time_sec(Double rdb_last_bgsave_time_sec) {
        this.rdb_last_bgsave_time_sec = rdb_last_bgsave_time_sec;
    }

    public Double getRdb_current_bgsave_time_sec() {
        return rdb_current_bgsave_time_sec;
    }

    public void setRdb_current_bgsave_time_sec(Double rdb_current_bgsave_time_sec) {
        this.rdb_current_bgsave_time_sec = rdb_current_bgsave_time_sec;
    }

    public Double getAof_enabled() {
        return aof_enabled;
    }

    public void setAof_enabled(Double aof_enabled) {
        this.aof_enabled = aof_enabled;
    }

    public Double getAof_rewrite_in_progress() {
        return aof_rewrite_in_progress;
    }

    public void setAof_rewrite_in_progress(Double aof_rewrite_in_progress) {
        this.aof_rewrite_in_progress = aof_rewrite_in_progress;
    }

    public Double getAof_rewrite_scheduled() {
        return aof_rewrite_scheduled;
    }

    public void setAof_rewrite_scheduled(Double aof_rewrite_scheduled) {
        this.aof_rewrite_scheduled = aof_rewrite_scheduled;
    }

    public Double getAof_last_rewrite_time_sec() {
        return aof_last_rewrite_time_sec;
    }

    public void setAof_last_rewrite_time_sec(Double aof_last_rewrite_time_sec) {
        this.aof_last_rewrite_time_sec = aof_last_rewrite_time_sec;
    }

    public Double getAof_current_rewrite_time_sec() {
        return aof_current_rewrite_time_sec;
    }

    public void setAof_current_rewrite_time_sec(Double aof_current_rewrite_time_sec) {
        this.aof_current_rewrite_time_sec = aof_current_rewrite_time_sec;
    }

    public Double getTotal_connections_received() {
        return total_connections_received;
    }

    public void setTotal_connections_received(Double total_connections_received) {
        this.total_connections_received = total_connections_received;
    }

    public Double getTotal_commands_processed() {
        return total_commands_processed;
    }

    public void setTotal_commands_processed(Double total_commands_processed) {
        this.total_commands_processed = total_commands_processed;
    }

    public Double getTotal_net_input_bytes() {
        return total_net_input_bytes;
    }

    public void setTotal_net_input_bytes(Double total_net_input_bytes) {
        this.total_net_input_bytes = total_net_input_bytes;
    }

    public Double getTotal_net_output_bytes() {
        return total_net_output_bytes;
    }

    public void setTotal_net_output_bytes(Double total_net_output_bytes) {
        this.total_net_output_bytes = total_net_output_bytes;
    }

    public Double getRejected_connections() {
        return rejected_connections;
    }

    public void setRejected_connections(Double rejected_connections) {
        this.rejected_connections = rejected_connections;
    }

    public Double getExpired_keys() {
        return expired_keys;
    }

    public void setExpired_keys(Double expired_keys) {
        this.expired_keys = expired_keys;
    }

    public Double getEvicted_keys() {
        return evicted_keys;
    }

    public void setEvicted_keys(Double evicted_keys) {
        this.evicted_keys = evicted_keys;
    }

    public Double getKeyspace_hits() {
        return keyspace_hits;
    }

    public void setKeyspace_hits(Double keyspace_hits) {
        this.keyspace_hits = keyspace_hits;
    }

    public Double getKeyspace_misses() {
        return keyspace_misses;
    }

    public void setKeyspace_misses(Double keyspace_misses) {
        this.keyspace_misses = keyspace_misses;
    }

    public Double getPubsub_channels() {
        return pubsub_channels;
    }

    public void setPubsub_channels(Double pubsub_channels) {
        this.pubsub_channels = pubsub_channels;
    }

    public Double getPubsub_patterns() {
        return pubsub_patterns;
    }

    public void setPubsub_patterns(Double pubsub_patterns) {
        this.pubsub_patterns = pubsub_patterns;
    }

    public Double getLoading() {
        return loading;
    }

    public void setLoading(Double loading) {
        this.loading = loading;
    }

    public Double getConnected_slaves() {
        return connected_slaves;
    }

    public void setConnected_slaves(Double connected_slaves) {
        this.connected_slaves = connected_slaves;
    }

    public Double getRepl_backlog_size() {
        return repl_backlog_size;
    }

    public void setRepl_backlog_size(Double repl_backlog_size) {
        this.repl_backlog_size = repl_backlog_size;
    }

    public Double getUsed_cpu_sys() {
        return used_cpu_sys;
    }

    public void setUsed_cpu_sys(Double used_cpu_sys) {
        this.used_cpu_sys = used_cpu_sys;
    }

    public Double getUsed_cpu_user() {
        return used_cpu_user;
    }

    public void setUsed_cpu_user(Double used_cpu_user) {
        this.used_cpu_user = used_cpu_user;
    }

    public Double getUsed_cpu_sys_children() {
        return used_cpu_sys_children;
    }

    public void setUsed_cpu_sys_children(Double used_cpu_sys_children) {
        this.used_cpu_sys_children = used_cpu_sys_children;
    }

    public Double getUsed_cpu_user_children() {
        return used_cpu_user_children;
    }

    public void setUsed_cpu_user_children(Double used_cpu_user_children) {
        this.used_cpu_user_children = used_cpu_user_children;
    }
}
