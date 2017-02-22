package com.gh.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by GH-GAN on 2016/12/8.
 */
public class ConfigUtil {
    public static String sparkmaster;
    public static int capStreamtime;
    public static int logStreamSaveTime;
    public static int capStreamSaveTime;
    public static String mysqlCapcheckpoint;
    public static String nginxCapcheckpoint;
    public static String redisCapcheckpoint;
    public static String containerCapcheckpoint;
    public static String saveCapcheckpoint;
    public static String saveLogcheckpoint;
    public static String brokers;
    public static String esnodes;
    public static String alertUrl;
    public static String gjGetUrl;

    static  {
//        InputStream in = ConfigUtil.class.getResourceAsStream("/conf_aws.properties");
        InputStream in = ConfigUtil.class.getResourceAsStream("/conf.properties");
//        InputStream in = ConfigUtil.class.getResourceAsStream("/conf_local.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            sparkmaster = prop.getProperty("sparkmaster");
            capStreamtime = Integer.parseInt(prop.getProperty("capStreamtime"));
            logStreamSaveTime = Integer.parseInt(prop.getProperty("logStreamSaveTime"));
            capStreamSaveTime = Integer.parseInt(prop.getProperty("capStreamSaveTime"));
            mysqlCapcheckpoint = prop.getProperty("mysqlCapcheckpoint");
            nginxCapcheckpoint = prop.getProperty("nginxCapcheckpoint");
            redisCapcheckpoint = prop.getProperty("redisCapcheckpoint");
            containerCapcheckpoint = prop.getProperty("containerCapcheckpoint");
            saveCapcheckpoint = prop.getProperty("saveCapcheckpoint");
            saveLogcheckpoint = prop.getProperty("saveLogcheckpoint");
            brokers = prop.getProperty("brokers");
            esnodes = prop.getProperty("esnodes");
            alertUrl = prop.getProperty("alertUrl");
            gjGetUrl = prop.getProperty("gjGetUrl");
        } catch (Exception e) {
            System.err.println("conf file load error !!!");
        }
    }

}
