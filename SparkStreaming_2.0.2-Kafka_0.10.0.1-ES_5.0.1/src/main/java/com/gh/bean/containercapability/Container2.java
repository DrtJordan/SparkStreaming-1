package com.gh.bean.containercapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class Container2 implements Serializable {
    String type;
    ContainerData2 data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContainerData2 getData() {
        return data;
    }

    public void setData(ContainerData2 data) {
        this.data = data;
    }
}
