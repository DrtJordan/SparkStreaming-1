package com.gh.bean.appcapability;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GH-GAN on 2016/11/25.
 */
public class AppMySqlData  implements Serializable {
    String timestamp;
    String container_uuid;
    String environment_id;
    String container_name;
    String namespace;
    ArrayList<AppMysqlStats> stats;

    public String getContainer_name() {
        return container_name;
    }

    public void setContainer_name(String container_name) {
        this.container_name = container_name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContainer_uuid() {
        return (null == container_uuid) ? "" : container_uuid;
    }

    public void setContainer_uuid(String container_uuid) {
        this.container_uuid = container_uuid;
    }

    public String getEnvironment_id() {
        return (null == environment_id) ? "" : environment_id;
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
