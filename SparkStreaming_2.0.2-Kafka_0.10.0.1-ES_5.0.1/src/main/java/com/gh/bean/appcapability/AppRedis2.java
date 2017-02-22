package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppRedis2 implements Serializable {
    String type;
    AppRedisData2 data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppRedisData2 getData() {
        return data;
    }

    public void setData(AppRedisData2 data) {
        this.data = data;
    }
}
