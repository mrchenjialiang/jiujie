package com.jiujie.base.util;

import java.util.Calendar;

/**
 * author : Created by ChenJiaLiang on 2016/9/21.
 * Email : 576507648@qq.com
 */
public class DateUtil {

    public static Calendar roll(Calendar calendar,int field, int value){
        if(value==0) return calendar;

        if(calendar==null){
            return null;
        }
        if(field==Calendar.YEAR){
            return rollYear(calendar, value);
        }else if(field==Calendar.MONTH){
            return rollMonth(calendar, value);
        }else if(field==Calendar.WEEK_OF_MONTH||field==Calendar.WEEK_OF_YEAR){
            return rollWeek(calendar, value);
        }else if(field==Calendar.DAY_OF_YEAR
                ||field==Calendar.DAY_OF_MONTH
                ||field==Calendar.DAY_OF_WEEK
                ||field==Calendar.DAY_OF_WEEK_IN_MONTH){
            return rollDay(calendar, value);
        }else{
            calendar.roll(field,value);
            return calendar;
        }
    }

    public static Calendar rollDay(Calendar calendar, int value) {
        if(value==0) return calendar;

        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        int daySizeOfYear = getDaySizeOfYear(calendar.get(Calendar.YEAR));

        int resultDayOfYear = currentDay+value;

        int rollYear = 0;
        while (resultDayOfYear<0||resultDayOfYear>daySizeOfYear){
            if(resultDayOfYear<0){
                resultDayOfYear+=daySizeOfYear;
                rollYear--;
            }else{
                resultDayOfYear-=daySizeOfYear;
                rollYear++;
            }
        }

        if(rollYear!=0) calendar = rollYear(calendar, rollYear);
        calendar.set(Calendar.DAY_OF_YEAR,resultDayOfYear);
        return calendar;
    }

    public static Calendar rollWeek(Calendar calendar, int value) {
        if(value==0) return calendar;

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int weekSizeOfYear = getWeekSizeOfYear(calendar.get(Calendar.YEAR));
        int resultWeek = currentWeek + value;

        int rollYear = 0;
        while (resultWeek<0||resultWeek>weekSizeOfYear){
            if(resultWeek<0){
                resultWeek+=weekSizeOfYear;
                rollYear--;
            }else{
                resultWeek-=weekSizeOfYear;
                rollYear++;
            }
        }

        if(rollYear!=0)  calendar = rollYear(calendar, rollYear);
        calendar.set(Calendar.WEEK_OF_YEAR, resultWeek);
        return calendar;
    }

    public static Calendar rollMonth(Calendar calendar, int value) {
        if(value==0) return calendar;

        int resultMonth = calendar.get(Calendar.MONTH) + value;
        int rollYear = 0;
        while (resultMonth<0||resultMonth>11){
            if(resultMonth<0){
                resultMonth+=11;
                rollYear--;
            }else{
                resultMonth-=11;
                rollYear++;
            }
        }
        int rollMonth = value%11;
        if(rollYear!=0)calendar = rollYear(calendar, rollYear);
        if(rollMonth!=0) calendar.roll(Calendar.MONTH, rollMonth);
        return calendar;
    }

    public static Calendar rollYear(Calendar calendar, int value) {
        if(value==0) return calendar;

        calendar.roll(Calendar.YEAR,value);
        return calendar;
    }

    public static int getWeekSizeOfYear(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.DAY_OF_YEAR, getDaySizeOfYear(year));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekSizeOfMonth(int year,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH, getDaySizeOfMonth(year,month));
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static int getDaySizeOfYear(int year) {
        int yearDays;
        if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){//闰年的判断规则
            yearDays=366;
        }else{
            yearDays=365;
        }
        return yearDays;
    }

    /**
     * 获取一个月有多少天
     * @param month Calendar.get(Calendar.Month)
     */
    public static int getDaySizeOfMonth(int year, int month){
        if (month > 11||month<0) {
            return 0;
        }
        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }
}

