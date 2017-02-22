package com.gh.bean.logfile;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class LogFileRedis  implements Serializable {
    String type;
    DataRedis data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataRedis getData() {
        return data;
    }

    public void setData(DataRedis data) {
        this.data = data;
    }
}
