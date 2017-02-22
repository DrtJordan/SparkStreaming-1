package com.gh.bean.logfile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class LogFileNginx  implements Serializable {
    String type;
    @JsonIgnore
    String date;
    DataNginx data;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataNginx getData() {
        return data;
    }

    public void setData(DataNginx data) {
        this.data = data;
    }


}
