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
    public static void Post(String url,String json){
        URLConnection conn;
        try {
            conn = new URL(url).openConnection();
            HttpURLConnection con = (HttpURLConnection) conn;
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            //写数据
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());;
            out.write(json);
            out.flush();

           /* int code = con.getResponseCode();
            System.out.println("code:"+code);*/

            // 读取返回的数据
           /* InputStream is = con.getInputStream();
            int len = 0;
            byte[] b = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while((len = is.read(b)) != -1){
                String s = new String(b,0,len,"UTF-8");
                sb.append(s);
            }
            System.out.println(sb.toString());*/

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
