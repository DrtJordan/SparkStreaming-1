package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppMySql2 implements Serializable {
    String type;
    AppMySqlData2 data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppMySqlData2 getData() {
        return data;
    }

    public void setData(AppMySqlData2 data) {
        this.data = data;
    }
}
