package com.gh.utils;

import com.gh.bean.GZ;
import com.gh.bean.alert.AlertDataInfo;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by GH-GAN on 2016/11/28.
 */
public class HttpUtil {
    URLConnection conn = null;
    private static Logger logger = Logger.getLogger(HttpUtil.class);

    public static Map<String,GZ> gz_map = new HashMap<String,GZ>();

    static {
        gz_map.put("app_mysql_connection",new GZ("connection","GT",0.89));
        gz_map.put("app_redis_memory",new GZ("memory","GT",0.8));
        gz_map.put("app_redis_hits",new GZ("hits","GT",0.6));
        gz_map.put("app_nginx_accept",new GZ("accept","LTE",0.8));

        gz_map.put("container_cpu",new GZ("cpu","GT",0.9));
        gz_map.put("container_memory",new GZ("memory","GT",0.7));
        gz_map.put("container_network_tx",new GZ("network_tx","GT",2.0));
        gz_map.put("container_network_rx",new GZ("network_rx","GT",2.0));
        gz_map.put("container_disk",new GZ("disk","GT",0.8));

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(new Runnable() {
            public void run() {
                initGJ();
            }
        },1,60, TimeUnit.SECONDS);

    }
    public static void initGJ(){
        String gz = HttpUtil.Get(ConfigUtil.gjGetUrl);
        JsonNode jsonNode = JsonUtil.getJsonNode(gz);
        JsonNode app = jsonNode.get("app");
        for (int i=0;i < app.size() - 1 ; i++){
            JsonNode jsonNode1 = app.get(i);
            String app_type = jsonNode1.get("app_type").asText();
            JsonNode app_param = jsonNode1.get("app_param");
            for (int j=0;j<app_param.size()-0;j++){
                String key = app_param.get(j).get("key").asText();
                String condition = app_param.get(j).get("condition").asText();
                Double value = app_param.get(j).get("value").asDouble();
                GZ gzs = new GZ(key,condition,value);
                gz_map.put("app" + "_" + app_type + "_" + key,gzs);
            }
        }
        // container
        JsonNode container = jsonNode.get("container");
        for (int i=0;i < container.size() - 1 ; i++){
            JsonNode jsonNode1 = container.get(i);
            String key = jsonNode1.get("key").asText();
            String condition = jsonNode1.get("condition").asText();
            Double value = jsonNode1.get("value").asDouble();
            GZ gzs = new GZ(key,condition,value);
            gz_map.put("container" + "_" + key,gzs);
        }

//        System.out.print("map--test:"+gz_map.get("app_mysql_connection").getValue());
    }

    public HttpURLConnection init(String url){
        HttpURLConnection con = null;
        try {
            conn = new URL(url).openConnection();
            con = (HttpURLConnection) conn;
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Accept-Charset", "utf-8");
            con.setDoOutput(true);
//            con.setDoInput(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public void Post(String json){
        HttpURLConnection con =  init(ConfigUtil.alertUrl);
        try {
            //写数据
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());;
            out.write(json);
            out.flush();

            logger.info("alert: \n" + json);
            logger.info("code: \n" + con.getResponseCode());

        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void alerts(AlertDataInfo [] alerts){
        final String json = AlertInfoUtil.toAlertJson(alerts);
//        System.out.println(json);
        new Thread(new Runnable() {
            public void run() {
                new HttpUtil().Post(json);
            }
        }).start();
    }

    public static String Get(String url){
        HttpURLConnection con2 = null;
        StringBuilder sb = new StringBuilder();
        try {
            con2 = (HttpURLConnection) new URL(url).openConnection();
            con2.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            String line = null;

            while ((line = in.readLine()) != null){
                sb.append(line.trim());
            }

        }catch (Exception e) {
            e.printStackTrace();
            con2.disconnect();
        }
        return sb.toString();
    }
}
