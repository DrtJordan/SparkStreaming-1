package com.gh.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class DateUtil {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static SimpleDateFormat df_utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static  TimeZone utc_0 = TimeZone.getTimeZone("UTC");

    static {
        df_utc.setTimeZone(utc_0);
    }

    public static String formatToUTC_0(String dateString){
        try {
            Date parse = df.parse(dateString);
           return df_utc.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatToUTC_8(String utc0String){
        try {
            Date parse = df_utc.parse(utc0String);
            return df.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getyyyyMMdd(String timestamp){
        return timestamp.substring(0,"yyyy-MM-dd".length());
    }
}
