package com.gh.utils;

import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by GH-GAN on 2016/11/24.
 */
public class DateUtil {

    /*public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static SimpleDateFormat df_utc_1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static SimpleDateFormat df_utc_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static SimpleDateFormat df_utc_3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static SimpleDateFormat df_utc_base3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSFFFFFF'Z'");

    public static  TimeZone utc_0 = TimeZone.getTimeZone("UTC");

    static {
        df_utc_1.setTimeZone(utc_0);
        df_utc_2.setTimeZone(utc_0);
        df_utc_3.setTimeZone(utc_0);
        df_utc_base3.setTimeZone(utc_0);
    }

    public static String formatToUTC_0(String timestap){
        try {
            Date parse = df.parse(timestap);
           return df_utc_2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatToGeneral(String timestap){
        try {
            Date parse = df_utc_2.parse(timestap);
            return df.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String baseUTCToGeneral(String timestap){
        try {
            Date parse = df_utc_1.parse(timestap);
            return df.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    public static String getYYYYMMdd(String timestamp){
        ZonedDateTime zdt = ZonedDateTime.parse(timestamp);
        ZonedDateTime localnow = ZonedDateTime.now();

        int local_offset_sec = localnow.getOffset().getTotalSeconds();
        int zdt_offset_sec = zdt.getOffset().getTotalSeconds();

        StringBuffer sb = new StringBuffer();
        if (local_offset_sec != zdt_offset_sec){
            int sec = local_offset_sec - zdt_offset_sec;
            ZonedDateTime _new_zdt = zdt.withZoneSameLocal(localnow.getZone());
            ZonedDateTime new_zdt = _new_zdt.plusSeconds(sec);
            sb.append(new_zdt.getYear());
            if (new_zdt.getMonthValue() < 10){
                sb.append("-0" + new_zdt.getMonthValue());
            }else {
                sb.append("-" + new_zdt.getMonthValue());
            }
            if (new_zdt.getDayOfMonth() < 10){
                sb.append("-0" + new_zdt.getDayOfMonth());
            }else {
                sb.append("-" + new_zdt.getDayOfMonth());
            }
        }else {
            sb.append(zdt.getYear());
            if (zdt.getMonthValue() < 10){
                sb.append("-0" + zdt.getMonthValue());
            }else {
                sb.append("-" + zdt.getMonthValue());
            }
            if (zdt.getDayOfMonth() < 10){
                sb.append("-0" + zdt.getDayOfMonth());
            }else {
                sb.append("-" + zdt.getDayOfMonth());
            }
        }
        return sb.toString();
    }
}
