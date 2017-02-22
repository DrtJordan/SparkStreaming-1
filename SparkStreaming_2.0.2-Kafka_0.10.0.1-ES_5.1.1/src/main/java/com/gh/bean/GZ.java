package com.gh.bean;

/**
 * Created by GH-GAN on 2016/12/7.
 */
public class GZ {
    String key;
    String condition;
    Double value;

    public GZ(String key, String condition, Double value) {
        this.key = key;
        this.condition = condition;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
