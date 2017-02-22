package com.gh.bean.containercapability;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class Container implements Serializable {
    String type;
    ContainerData [] data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContainerData[] getData() {
        return data;
    }

    public void setData(ContainerData[] data) {
        this.data = data;
    }
}
