package com.gh.bean.containercapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/12/6.
 */
public class ContainerFileSystem implements Serializable{
    String container_filesystem_name;
    String container_filesystem_type;
    Double container_filesystem_capacity;
    Double container_filesystem_usage;

    public ContainerFileSystem(){}

    public ContainerFileSystem(String container_filesystem_name, String container_filesystem_type, Double container_filesystem_capacity, Double container_filesystem_usage) {
        this.container_filesystem_name = container_filesystem_name;
        this.container_filesystem_type = container_filesystem_type;
        this.container_filesystem_capacity = container_filesystem_capacity;
        this.container_filesystem_usage = container_filesystem_usage;
    }

    public String getContainer_filesystem_name() {
        return container_filesystem_name;
    }

    public void setContainer_filesystem_name(String container_filesystem_name) {
        this.container_filesystem_name = container_filesystem_name;
    }

    public String getContainer_filesystem_type() {
        return container_filesystem_type;
    }

    public void setContainer_filesystem_type(String container_filesystem_type) {
        this.container_filesystem_type = container_filesystem_type;
    }

    public Double getContainer_filesystem_capacity() {
        return container_filesystem_capacity;
    }

    public void setContainer_filesystem_capacity(Double container_filesystem_capacity) {
        this.container_filesystem_capacity = container_filesystem_capacity;
    }

    public Double getContainer_filesystem_usage() {
        return container_filesystem_usage;
    }

    public void setContainer_filesystem_usage(Double container_filesystem_usage) {
        this.container_filesystem_usage = container_filesystem_usage;
    }
}
