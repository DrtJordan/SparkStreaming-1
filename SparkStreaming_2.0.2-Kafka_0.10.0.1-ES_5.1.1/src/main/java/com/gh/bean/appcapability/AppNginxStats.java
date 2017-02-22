package com.gh.bean.appcapability;

import java.io.Serializable;

/**
 * Created by GH-GAN on 2016/11/29.
 */
public class AppNginxStats  implements Serializable {
    String timestamp;
    Double active_connections;
    Double accepts;
    Double handled;
    Double requests;
    Double reading;
    Double witing;
    Double waiting;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getActive_connections() {
        return active_connections;
    }

    public void setActive_connections(Double active_connections) {
        this.active_connections = active_connections;
    }

    public Double getAccepts() {
        return accepts;
    }

    public void setAccepts(Double accepts) {
        this.accepts = accepts;
    }

    public Double getHandled() {
        return handled;
    }

    public void setHandled(Double handled) {
        this.handled = handled;
    }

    public Double getRequests() {
        return requests;
    }

    public void setRequests(Double requests) {
        this.requests = requests;
    }

    public Double getReading() {
        return reading;
    }

    public void setReading(Double reading) {
        this.reading = reading;
    }

    public Double getWiting() {
        return witing;
    }

    public void setWiting(Double witing) {
        this.witing = witing;
    }

    public Double getWaiting() {
        return waiting;
    }

    public void setWaiting(Double waiting) {
        this.waiting = waiting;
    }
}
