package com.czh.life_assistant.util;

public class GetWeekday {
    public static String getWeekday(int num) {
        num = num % 7;
        String week = null;
        if (num == 1) {
            //week = "Sunday";
            week = "星期日  Sun.";
            return week;
        } else if (num == 2) {
            //week = "Monday";
            week = "星期一  Mon.";
            return week;
        } else if (num == 3) {
            //week = "Tuesday";
            week = "星期二  Tues.";
            return week;
        } else if (num == 4) {
            //week = "Wednesday";
            week = "星期三  Wed.";
            return week;
        } else if (num == 5) {
            //week = "Thursday";
            week = "星期四  Thur.";
            return week;
        } else if (num == 6) {
            //week = "Friday";
            week = "星期五  Fri.";
            return week;
        } else {
            //week = "Saturday";
            week = "星期六  Sat.";
            return week;
        }
    }

    public static String getWeekdayZh(int num) {
        num = num % 7;
        String week = null;
        if (num == 1) {
            week = "周日";
            return week;
        } else if (num == 2) {
            week = "周一";
            return week;
        } else if (num == 3) {
            week = "周二";
            return week;
        } else if (num == 4) {
            week = "周三";
            return week;
        } else if (num == 5) {
            week = "周四";
            return week;
        } else if (num == 6) {
            week = "周五";
            return week;
        } else {
            week = "周六";
            return week;
        }
    }
}
