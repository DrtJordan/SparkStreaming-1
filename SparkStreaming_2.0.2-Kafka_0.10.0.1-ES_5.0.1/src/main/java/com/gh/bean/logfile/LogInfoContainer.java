package com.gh.bean.logfile;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class LogInfoContainer {
    String log_time;
    String source;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }

}
