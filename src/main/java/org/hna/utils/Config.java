package org.hna.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by HP on 2016/2/16.
 */
public class Config {
    private static Logger log = LoggerFactory.getLogger(Config.class);
    public static String url;
    public static String userName;
    public static String userPasswd;
    public static String master;
    public static String checkpoint;
    public static String zklist;
    public static String kafkatopic;

    public static String cdh = "cdh_";
    public static String pre = "standalone_";
    public static String env = pre;  // 切换环境

    static{
            InputStream in = Config.class.getResourceAsStream("/conf.properties");
            Properties prop = new Properties();
            try{
                prop.load(in);

                url = prop.getProperty(env+"jdbcUrl");
                userName = prop.getProperty(env+"userName");
                userPasswd = prop.getProperty(env+"userPasswd");

                master = prop.getProperty(env+"master");
                checkpoint = prop.getProperty(env+"checkpoint");
                zklist = prop.getProperty(env+"zklist");
                kafkatopic = prop.getProperty(env+"kafkatopic");

            }catch(Exception e){
                log.error("conf file load error !!!");
            }
    }

}
