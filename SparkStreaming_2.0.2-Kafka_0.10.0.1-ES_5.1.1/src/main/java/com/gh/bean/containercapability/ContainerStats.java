package com.gh.bean.containercapability;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GH-GAN on 2016/11/29.
 */
public class ContainerStats implements Serializable {
    String timestamp;
    Double container_cpu_usage_seconds_total;
    Double container_cpu_user_seconds_total;
    Double container_cpu_system_seconds_total;
    Double container_memory_usage_bytes;
    Double container_memory_limit_bytes;
    Double container_memory_cache;
    Double container_memory_rss;
    Double container_memory_swap;
    Double container_network_receive_bytes_total;
    Double container_network_receive_packets_total;
    Double container_network_receive_packets_dropped_total;
    Double container_network_receive_errors_total;
    Double container_network_transmit_bytes_total;
    Double container_network_transmit_packets_total;
    Double container_network_transmit_packets_dropped_total;
    Double container_network_transmit_errors_total;
    ContainerFileSystem [] container_filesystem;
    Double container_diskio_service_bytes_async;
    Double container_diskio_service_bytes_read;
    Double container_diskio_service_bytes_sync;
    Double container_diskio_service_bytes_total;
    Double container_diskio_service_bytes_write;
    Double container_tasks_state_nr_sleeping;
    Double container_tasks_state_nr_running;
    Double container_tasks_state_nr_stopped;
    Double container_tasks_state_nr_uninterruptible;
    Double container_tasks_state_nr_io_wait;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getContainer_cpu_usage_seconds_total() {
        return container_cpu_usage_seconds_total;
    }

    public void setContainer_cpu_usage_seconds_total(Double container_cpu_usage_seconds_total) {
        this.container_cpu_usage_seconds_total = container_cpu_usage_seconds_total;
    }

    public Double getContainer_cpu_user_seconds_total() {
        return container_cpu_user_seconds_total;
    }

    public void setContainer_cpu_user_seconds_total(Double container_cpu_user_seconds_total) {
        this.container_cpu_user_seconds_total = container_cpu_user_seconds_total;
    }

    public Double getContainer_cpu_system_seconds_total() {
        return container_cpu_system_seconds_total;
    }

    public void setContainer_cpu_system_seconds_total(Double container_cpu_system_seconds_total) {
        this.container_cpu_system_seconds_total = container_cpu_system_seconds_total;
    }

    public Double getContainer_memory_usage_bytes() {
        return container_memory_usage_bytes;
    }

    public void setContainer_memory_usage_bytes(Double container_memory_usage_bytes) {
        this.container_memory_usage_bytes = container_memory_usage_bytes;
    }

    public Double getContainer_memory_limit_bytes() {
        return container_memory_limit_bytes;
    }

    public void setContainer_memory_limit_bytes(Double container_memory_limit_bytes) {
        this.container_memory_limit_bytes = container_memory_limit_bytes;
    }

    public Double getContainer_memory_cache() {
        return container_memory_cache;
    }

    public void setContainer_memory_cache(Double container_memory_cache) {
        this.container_memory_cache = container_memory_cache;
    }

    public Double getContainer_memory_rss() {
        return container_memory_rss;
    }

    public void setContainer_memory_rss(Double container_memory_rss) {
        this.container_memory_rss = container_memory_rss;
    }

    public Double getContainer_memory_swap() {
        return container_memory_swap;
    }

    public void setContainer_memory_swap(Double container_memory_swap) {
        this.container_memory_swap = container_memory_swap;
    }

    public Double getContainer_network_receive_bytes_total() {
        return container_network_receive_bytes_total;
    }

    public void setContainer_network_receive_bytes_total(Double container_network_receive_bytes_total) {
        this.container_network_receive_bytes_total = container_network_receive_bytes_total;
    }

    public Double getContainer_network_receive_packets_total() {
        return container_network_receive_packets_total;
    }

    public void setContainer_network_receive_packets_total(Double container_network_receive_packets_total) {
        this.container_network_receive_packets_total = container_network_receive_packets_total;
    }

    public Double getContainer_network_receive_packets_dropped_total() {
        return container_network_receive_packets_dropped_total;
    }

    public void setContainer_network_receive_packets_dropped_total(Double container_network_receive_packets_dropped_total) {
        this.container_network_receive_packets_dropped_total = container_network_receive_packets_dropped_total;
    }

    public Double getContainer_network_receive_errors_total() {
        return container_network_receive_errors_total;
    }

    public void setContainer_network_receive_errors_total(Double container_network_receive_errors_total) {
        this.container_network_receive_errors_total = container_network_receive_errors_total;
    }

    public Double getContainer_network_transmit_bytes_total() {
        return container_network_transmit_bytes_total;
    }

    public void setContainer_network_transmit_bytes_total(Double container_network_transmit_bytes_total) {
        this.container_network_transmit_bytes_total = container_network_transmit_bytes_total;
    }

    public Double getContainer_network_transmit_packets_total() {
        return container_network_transmit_packets_total;
    }

    public void setContainer_network_transmit_packets_total(Double container_network_transmit_packets_total) {
        this.container_network_transmit_packets_total = container_network_transmit_packets_total;
    }

    public Double getContainer_network_transmit_packets_dropped_total() {
        return container_network_transmit_packets_dropped_total;
    }

    public void setContainer_network_transmit_packets_dropped_total(Double container_network_transmit_packets_dropped_total) {
        this.container_network_transmit_packets_dropped_total = container_network_transmit_packets_dropped_total;
    }

    public Double getContainer_network_transmit_errors_total() {
        return container_network_transmit_errors_total;
    }

    public void setContainer_network_transmit_errors_total(Double container_network_transmit_errors_total) {
        this.container_network_transmit_errors_total = container_network_transmit_errors_total;
    }

    public ContainerFileSystem[] getContainer_filesystem() {
        return container_filesystem;
    }

    public void setContainer_filesystem(ContainerFileSystem[] container_filesystem) {
        this.container_filesystem = container_filesystem;
    }

    public Double getContainer_diskio_service_bytes_async() {
        return container_diskio_service_bytes_async;
    }

    public void setContainer_diskio_service_bytes_async(Double container_diskio_service_bytes_async) {
        this.container_diskio_service_bytes_async = container_diskio_service_bytes_async;
    }

    public Double getContainer_diskio_service_bytes_read() {
        return container_diskio_service_bytes_read;
    }

    public void setContainer_diskio_service_bytes_read(Double container_diskio_service_bytes_read) {
        this.container_diskio_service_bytes_read = container_diskio_service_bytes_read;
    }

    public Double getContainer_diskio_service_bytes_sync() {
        return container_diskio_service_bytes_sync;
    }

    public void setContainer_diskio_service_bytes_sync(Double container_diskio_service_bytes_sync) {
        this.container_diskio_service_bytes_sync = container_diskio_service_bytes_sync;
    }

    public Double getContainer_diskio_service_bytes_total() {
        return container_diskio_service_bytes_total;
    }

    public void setContainer_diskio_service_bytes_total(Double container_diskio_service_bytes_total) {
        this.container_diskio_service_bytes_total = container_diskio_service_bytes_total;
    }

    public Double getContainer_diskio_service_bytes_write() {
        return container_diskio_service_bytes_write;
    }

    public void setContainer_diskio_service_bytes_write(Double container_diskio_service_bytes_write) {
        this.container_diskio_service_bytes_write = container_diskio_service_bytes_write;
    }

    public Double getContainer_tasks_state_nr_sleeping() {
        return container_tasks_state_nr_sleeping;
    }

    public void setContainer_tasks_state_nr_sleeping(Double container_tasks_state_nr_sleeping) {
        this.container_tasks_state_nr_sleeping = container_tasks_state_nr_sleeping;
    }

    public Double getContainer_tasks_state_nr_running() {
        return container_tasks_state_nr_running;
    }

    public void setContainer_tasks_state_nr_running(Double container_tasks_state_nr_running) {
        this.container_tasks_state_nr_running = container_tasks_state_nr_running;
    }

    public Double getContainer_tasks_state_nr_stopped() {
        return container_tasks_state_nr_stopped;
    }

    public void setContainer_tasks_state_nr_stopped(Double container_tasks_state_nr_stopped) {
        this.container_tasks_state_nr_stopped = container_tasks_state_nr_stopped;
    }

    public Double getContainer_tasks_state_nr_uninterruptible() {
        return container_tasks_state_nr_uninterruptible;
    }

    public void setContainer_tasks_state_nr_uninterruptible(Double container_tasks_state_nr_uninterruptible) {
        this.container_tasks_state_nr_uninterruptible = container_tasks_state_nr_uninterruptible;
    }

    public Double getContainer_tasks_state_nr_io_wait() {
        return container_tasks_state_nr_io_wait;
    }

    public void setContainer_tasks_state_nr_io_wait(Double container_tasks_state_nr_io_wait) {
        this.container_tasks_state_nr_io_wait = container_tasks_state_nr_io_wait;
    }
}
