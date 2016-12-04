package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppNginx  implements Serializable {
    String type;
    AppNginxData data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppNginxData getData() {
        return data;
    }

    public void setData(AppNginxData data) {
        this.data = data;
    }
}
