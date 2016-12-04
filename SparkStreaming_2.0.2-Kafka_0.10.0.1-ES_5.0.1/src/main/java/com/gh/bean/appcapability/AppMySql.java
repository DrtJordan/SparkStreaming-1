package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppMySql implements Serializable {
    String type;
    AppMySqlData data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppMySqlData getData() {
        return data;
    }

    public void setData(AppMySqlData data) {
        this.data = data;
    }
}
