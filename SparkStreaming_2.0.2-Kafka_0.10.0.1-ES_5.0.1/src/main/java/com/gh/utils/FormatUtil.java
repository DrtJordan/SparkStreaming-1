package com.gh.utils;

import com.gh.bean.AlertDataInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GH-GAN on 2016/11/29.
 */
public class FormatUtil {

    public static String toMySqlWarnJson(String key,String start_time,String end_time,Double con_threshold){
        String[] keys = key.split("#");
        String environment_id = keys[0];
        String container_uuid = keys[1];
        String _type = keys[2];

        AlertDataInfo warn = new AlertDataInfo();
        warn.setStatus("success");
        warn.setAlert_type("M");
        warn.setAlert_dim("A");
        warn.setApp_type(_type);
        warn.setEnvironment_id(environment_id);
        warn.setContainer_uuid(container_uuid);
        warn.setStart_time(start_time);
        warn.setEnd_time(end_time);
        Map<String,Object> datas = new HashMap<String, Object>();
        datas.put("thread_connected_max_connections",con_threshold);
        warn.setData(datas);

        return JsonUtil.formatJson(warn);
    }

}
