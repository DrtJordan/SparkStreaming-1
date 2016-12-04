package com.gh.utils;

import com.gh.bean.alert.AlertDataInfo;
import com.gh.bean.alert.KeyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GH-GAN on 2016/11/29.
 */
public class AlertInfoUtil {
    public final static String SUCCESS = "success";
    public final static String FAILED = "failed";
    public final static String ALERT_TYPE_M = "M";
    public final static String ALERT_TYPE_L = "L";
    public final static String ALERT_DIM_C = "C";
    public final static String ALERT_DIM_A = "A";

    public static AlertDataInfo toWarnBean(String status, String alert_type, String alert_dim, String key, String start_time, String end_time, ArrayList<KeyValue> list){
        String[] keys = key.split("#");
        String environment_id = keys[0];
        String container_uuid = keys[1];
        String _type = keys[2];

        AlertDataInfo warn = new AlertDataInfo();
        warn.setStatus(status);
        warn.setAlert_type(alert_type);
        warn.setAlert_dim(alert_dim);
        warn.setApp_type(_type);
        warn.setEnvironment_id(environment_id);
        warn.setContainer_uuid(container_uuid);
        warn.setStart_time(start_time);
        warn.setEnd_time(end_time);
        warn.setData(list);

        return warn;
    }

}
