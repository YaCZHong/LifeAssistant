package com.czh.life_assistant.util;

public class GetWind {

    public static String getWindDir(double direction) {
        String Wind_Dir;
        if (direction < 5) {
            Wind_Dir = "北风";
            return Wind_Dir;
        }
        if (5 <= direction && direction < 85) {
            Wind_Dir = "东北风";
            return Wind_Dir;
        }
        if (85 <= direction && direction < 95) {
            Wind_Dir = "东风";
            return Wind_Dir;
        }
        if (95 <= direction && direction < 175) {
            Wind_Dir = "东南风";
            return Wind_Dir;
        }
        if (175 <= direction && direction < 185) {
            Wind_Dir = "南风";
            return Wind_Dir;
        }
        if (185 <= direction && direction < 265) {
            Wind_Dir = "西南风";
            return Wind_Dir;
        }
        if (265 <= direction && direction < 275) {
            Wind_Dir = "西风";
            return Wind_Dir;
        }
        if (275 <= direction && direction < 355) {
            Wind_Dir = "西北风";
            return Wind_Dir;
        } else {
            Wind_Dir = "北风";
            return Wind_Dir;
        }
    }

    public static String getWindSc(double speed) {
        String Wind_Sc;
        if (speed < 1) {
            Wind_Sc = 0 + " 级";
            return Wind_Sc;
        }
        if (1 <= speed && speed < 6) {
            Wind_Sc = 1 + " 级";
            return Wind_Sc;
        }
        if (6 <= speed && speed < 12) {
            Wind_Sc = 2 + " 级";
            return Wind_Sc;
        }
        if (12 <= speed && speed < 20) {
            Wind_Sc = 3 + " 级";
            return Wind_Sc;
        }
        if (20 <= speed && speed < 29) {
            Wind_Sc = 4 + " 级";
            return Wind_Sc;
        }
        if (29 <= speed && speed < 39) {
            Wind_Sc = 5 + " 级";
            return Wind_Sc;
        }
        if (39 <= speed && speed < 50) {
            Wind_Sc = 6 + " 级";
            return Wind_Sc;
        }
        if (50 <= speed && speed < 62) {
            Wind_Sc = 7 + " 级";
            return Wind_Sc;
        }
        if (62 <= speed && speed < 75) {
            Wind_Sc = 8 + " 级";
            return Wind_Sc;
        }
        if (75 <= speed && speed < 89) {
            Wind_Sc = 9 + " 级";
            return Wind_Sc;
        }
        if (89 <= speed && speed < 103) {
            Wind_Sc = 10 + " 级";
            return Wind_Sc;
        }
        if (103 <= speed && speed < 117) {
            Wind_Sc = 11 + " 级";
            return Wind_Sc;
        }
        if (117 <= speed && speed < 134) {
            Wind_Sc = 12 + " 级";
            return Wind_Sc;
        }
        if (134 <= speed && speed < 150) {
            Wind_Sc = 13 + " 级";
            return Wind_Sc;
        }
        if (150 <= speed && speed < 167) {
            Wind_Sc = 14 + " 级";
            return Wind_Sc;
        }
        if (167 <= speed && speed < 184) {
            Wind_Sc = 15 + " 级";
            return Wind_Sc;
        }
        if (184 <= speed && speed < 202) {
            Wind_Sc = 16 + " 级";
            return Wind_Sc;
        }
        if (202 <= speed && speed < 221) {
            Wind_Sc = 17 + " 级";
            return Wind_Sc;
        } else {
            Wind_Sc = 17 + " 级以上";
            return Wind_Sc;
        }
    }
}
