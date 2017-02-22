package com.gh.bean.alert;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/12/1.
 */
public class KeyValue  implements Serializable {
    String p_key;
    String p_value;

    public KeyValue() {
    }

    public KeyValue(String p_key, String p_value) {
        this.p_key = p_key;
        this.p_value = p_value;
    }

    public String getP_key() {
        return p_key;
    }

    public void setP_key(String p_key) {
        this.p_key = p_key;
    }

    public String getP_value() {
        return p_value;
    }

    public void setP_value(String p_value) {
        this.p_value = p_value;
    }
}
