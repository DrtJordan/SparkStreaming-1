package com.gh.bean.logfile;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class LogFileNginx  implements Serializable {
    String type;
    DataNginx data;

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
