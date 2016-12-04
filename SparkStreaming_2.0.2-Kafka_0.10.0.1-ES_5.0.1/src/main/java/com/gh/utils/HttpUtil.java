package com.gh.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by GH-GAN on 2016/11/28.
 */
public class HttpUtil {
    public static URLConnection conn = null;
    public static HttpURLConnection con = null;
    public static String URL = "http://223.202.32.56:8077/alert/v1/info/receive";

    public static void init(String url){
        try {
            conn = new URL(url).openConnection();
            con = (HttpURLConnection) conn;
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Accept-Charset", "utf-8");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setDoOutput(true);
            con.setDoInput(true);
        } catch (Exception e) {
            e.printStackTrace();
            con = null;
        }
    }

    static {
        init(URL);
    }

    public static void Post(String json){
            try {
                if (con == null) init(URL);

                //写数据
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());;
                out.write("alert="+json);
                out.flush();

                int code = con.getResponseCode();
                System.out.println("code:"+code);

            }catch (Exception e) {
                e.printStackTrace();
                con.disconnect();
                con = null;
            }

    }
}
