package com.gh.bean.alert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AlertData  implements Serializable {
    AlertDataInfo [] alert_data;

    public AlertDataInfo[] getAlert_data() {
        return alert_data;
    }

    public void setAlert_data(AlertDataInfo[] alert_data) {
        this.alert_data = alert_data;
    }
}
