package com.gh.bean.logfile;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class LogFileMySql  implements Serializable {
    String type;
    DataMySql data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataMySql getData() {
        return data;
    }

    public void setData(DataMySql data) {
        this.data = data;
    }
}
