package com.czh.life_assistant.util;

/**
 * Created by czh on 2018/5/17.
 */

public class GetWeatherTxtUtil {
    public static String getWeatherTxt(String status) {
        String txt;
        if (status.equals("CLEAR_DAY")) {
            txt = "晴天";
            return txt;
        }
        if (status.equals("CLEAR_NIGHT")) {
            txt = "晴夜";
            return txt;
        }
        if (status.equals("PARTLY_CLOUDY_DAY")) {
            txt = "多云";
            return txt;
        }
        if (status.equals("PARTLY_CLOUDY_NIGHT")) {
            txt = "多云";
            return txt;
        }
        if (status.equals("CLOUDY")) {
            txt = "阴";
            return txt;
        }
        if (status.equals("RAIN")) {
            txt = "雨";
            return txt;
        }
        if (status.equals("SNOW")) {
            txt = "雪";
            return txt;
        }
        if (status.equals("WIND")) {
            txt = "风";
            return txt;
        }
        if (status.equals("HAZE")) {
            txt = "霾";
            return txt;
        } else {
            txt = "未知";
            return txt;
        }
    }
}
