package com.jiujie.jiujie;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SimpleText {

    public static void main(String[] args) {

        //2018/4/7 16:10:59
        String time = timeLongHaoMiaoToString(1523088659000l, "yyyy-MM-dd HH:mm:ss");

        System.out.println(time);
        System.out.println(getCurrentTimeZoneId());
    }

    /**
     * 毫秒 时间戳转换为String
     */
    public static String timeLongHaoMiaoToString(long millis, String timeFromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
//        sdf.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZoneId()));
        return sdf.format(new Date(millis));
    }

    /**
     * 获取当前时区
     */
    public static String getCurrentTimeZoneId() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }
}
