package com.gh.bean.logfile;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class LogFileMySql  implements Serializable {
    String type;
    @JsonIgnore
    String date = "";
    DataMySql data;
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

    public DataMySql getData() {
        return data;
    }

    public void setData(DataMySql data) {
        this.data = data;
    }
}
