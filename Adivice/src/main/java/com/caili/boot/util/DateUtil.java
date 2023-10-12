package com.caili.boot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMD_2 = "yyyyMMdd";
    public static final String YMDHM = "yyyy-MM-dd HH:mm";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMS_2 = "yyyyMMddHHmmss";
    public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss,SSS";

    public static java.util.Date addMonth(java.util.Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, num);
        return cal.getTime();
    }

    public static java.util.Date addDay(java.util.Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, day);
        return c.getTime();
    }

    public static String formatDate() {
        return formatDate(new Date());
    }

    public static String formatDate(String fmt) {
        return formatDate(fmt, new Date());
    }

    public static String formatDate(long time) {
        return formatDate("yyyy-MM-dd HH:mm:ss", new Date(time));
    }

    public static String formatDate(Date date) {
        return formatDate("yyyy-MM-dd HH:mm:ss", date);
    }

    public static String formatDate(String fmt, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }

    public static String formatDate2(Date date,String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }

    public static Date parseDate(String dateStr) {
        return parseDate("yyyy-MM-dd HH:mm:ss", dateStr);
    }

    public static Date parseDate(String fmt, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object formatDate2String(Date date, String string) {
        SimpleDateFormat formatter = new SimpleDateFormat(string);

        return formatter.format(date);
    }

    public static String formatDatetoChina(Date date) {
        String day = formatDate(date);
        return formatStringtoChina(day);
    }

    public static String formatStringtoChina(String day) {
        String[] a = day.split(" ");
        String[] b = a[0].split("-");
        StringBuilder str = new StringBuilder();
        str.append(b[0]).append("年").append(b[1]).append("月").append(b[2]).append("日").append(" ").append(a[1]);
        return str.toString();
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDatetoChina(new Date()));
    }
}
