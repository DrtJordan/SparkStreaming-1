package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppRedis  implements Serializable {
    String type;
    AppRedisData data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppRedisData getData() {
        return data;
    }

    public void setData(AppRedisData data) {
        this.data = data;
    }
}
