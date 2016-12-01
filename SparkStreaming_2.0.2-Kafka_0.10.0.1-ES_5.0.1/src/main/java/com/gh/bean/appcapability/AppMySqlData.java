package com.gh.bean.appcapability;

import java.util.ArrayList;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppMySqlData {
    String container_uuid;
    String environment_id;
    ArrayList<AppMysqlStats> stats;

    public String getContainer_uuid() {
        return container_uuid;
    }

    public void setContainer_uuid(String container_uuid) {
        this.container_uuid = container_uuid;
    }

    public String getEnvironment_id() {
        return environment_id;
    }

    public void setEnvironment_id(String environment_id) {
        this.environment_id = environment_id;
    }

    public ArrayList<AppMysqlStats> getStats() {
        return stats;
    }

    public void setStats(ArrayList<AppMysqlStats> stats) {
        this.stats = stats;
    }
}
