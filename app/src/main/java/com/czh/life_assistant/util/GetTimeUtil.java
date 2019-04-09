package com.czh.life_assistant.util;

import java.util.Calendar;

public class GetTimeUtil {

    public static String getCurrentTime() {
        Calendar time = Calendar.getInstance();
        int time_hour = time.get(Calendar.HOUR_OF_DAY);
        int time_minute = time.get(Calendar.MINUTE);
        String hour;
        String minute;
        String current_time;
        if (time_minute < 10) {
            minute = "0" + time_minute;
        } else {
            minute = String.valueOf(time_minute);
        }
        if (time_hour < 10) {
            hour = "0" + time_hour;
        } else {
            hour = String.valueOf(time_hour);
        }
        current_time = hour + ":" + minute;
        return current_time;
    }
}
