package com.gh.bean.logfile;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class DataRedis  implements Serializable {
    String container_uuid;
    String environment_id;
    String app_file;
    String timestamp;
    LogInfoRedis log_info;

    public String getApp_file() {
        return app_file;
    }

    public void setApp_file(String app_file) {
        this.app_file = app_file;
    }

    public String getContainer_uuid() {
        return container_uuid;
    }

    public void setContainer_uuid(String container_uuid) {
        this.container_uuid = container_uuid;
    }

    public String getEnvironment_id() {
        return environment_id;
    }

    public void setEnvironment_id(String environment_id) {
        this.environment_id = environment_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LogInfoRedis getLog_info() {
        return log_info;
    }

    public void setLog_info(LogInfoRedis log_info) {
        this.log_info = log_info;
    }
}
