package com.czh.life_assistant.util;

public class GetAQIqltyUtil {
    public static String getAqiQlty(double aqi) {
        String Aqi_Qlty;
        if (aqi <= 50) {
            Aqi_Qlty = "优";
            return Aqi_Qlty;
        }
        if (50 < aqi && aqi <= 100) {
            Aqi_Qlty = "良好";
            return Aqi_Qlty;
        }
        if (100 < aqi && aqi <= 150) {
            Aqi_Qlty = "轻度污染";
            return Aqi_Qlty;
        }
        if (150 < aqi && aqi <= 200) {
            Aqi_Qlty = "中度污染";
            return Aqi_Qlty;
        } else {
            Aqi_Qlty = "重度污染";
            return Aqi_Qlty;
        }
    }

}
