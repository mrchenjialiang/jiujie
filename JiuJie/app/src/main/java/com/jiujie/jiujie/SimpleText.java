package com.jiujie.jiujie;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SimpleText {

    public static void main(String[] args) {

        String time = timeLongHaoMiaoToString(1522724777000l, "yyyy-MM-dd HH:mm:ss");

        System.out.println(time);
    }

    /**
     * 毫秒 时间戳转换为String
     */
    public static String timeLongHaoMiaoToString(long millis, String timeFromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf.format(new Date(millis));
    }
}
