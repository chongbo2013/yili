package com.lots.travel.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/10/29.
 */

public class TimeUtil {

    public static void getTime(long duration,int[] result){
        if(result==null || result.length!=3)
            throw new IllegalArgumentException("参数错误");

        duration = duration/1000L;
        int hour = (int) (duration/3600L);
        int odd = (int) (duration%3600L);
        int minute = odd/60;
        int second = odd%60;
        result[0] = hour;
        result[1] = minute;
        result[2] = second;
    }

    public static String getConstellation(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getConstellation(cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getConstellation(int month,int day){
        int point = -1;
        String[] str = {"白羊座","金牛座","双子座","巨蟹座","狮子座","处女座","天枰座","天蝎座","射手座","摩羯座","水瓶座","双鱼座"};
        //格式化日期,将日期转成**.**的格式,比如1月1日转成1.01
        Double date = Double.parseDouble((month + 1)
                + "." + String.format(Locale.getDefault(),"%02d", day));

        if (3.21 <= date && 4.19 >= date) {
            point = 0;
        } else if (4.20 <= date && 5.20 >= date) {
            point = 1;
        } else if (5.21 <= date && 6.21 >= date) {
            point = 2;
        } else if (6.22 <= date && 7.22 >= date) {
            point = 3;
        } else if (7.23 <= date && 8.22 >= date) {
            point = 4;
        } else if (8.23 <= date && 9.22 >= date) {
            point = 5;
        } else if (9.23 <= date && 10.23 >= date) {
            point = 6;
        } else if (10.24 <= date && 11.22 >= date) {
            point = 7;
        } else if (11.23 <= date && 12.21 >= date) {
            point = 8;
        } else if (12.22 <= date && 12.31 >= date) {
            point = 9;
        } else if (1.01 <= date && 1.19 >= date) {
            point = 9;
        } else if (1.20 <= date && 2.18 >= date) {
            point = 10;
        } else if (2.19 <= date && 3.20 >= date) {
            point = 11;
        }

        return str[point];
    }

    public static int getBetweenYears(Date d1,Date d2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        int years = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        return Math.abs(years);
    }

    public static int getBetweenMonths(Date d1,Date d2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        int result = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
        int month = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

}
