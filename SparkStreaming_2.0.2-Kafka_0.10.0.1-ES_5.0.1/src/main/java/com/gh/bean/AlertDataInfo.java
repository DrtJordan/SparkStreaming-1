package com.gh.bean;

import java.util.Map;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AlertDataInfo {
    String status;
    String alert_type;
    String alert_dim;
    String app_type;
    String msg;
    String environment_id;
    String container_uuid;
    String start_time;
    String end_time;
    Map<String,Object> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlert_type() {
        return alert_type;
    }

    public void setAlert_type(String alert_type) {
        this.alert_type = alert_type;
    }

    public String getAlert_dim() {
        return alert_dim;
    }

    public void setAlert_dim(String alert_dim) {
        this.alert_dim = alert_dim;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEnvironment_id() {
        return environment_id;
    }

    public void setEnvironment_id(String environment_id) {
        this.environment_id = environment_id;
    }

    public String getContainer_uuid() {
        return container_uuid;
    }

    public void setContainer_uuid(String container_uuid) {
        this.container_uuid = container_uuid;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
