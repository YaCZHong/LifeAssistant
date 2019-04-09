package com.czh.life_assistant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtil {
    public static void saveInfoToPrefs(Context context, String infoName, String Info) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(infoName, Info);
        editor.apply();
    }

    public static String getInfoFromPrefs(Context context, String infoName) {
        String Info;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Info = prefs.getString(infoName, null);
        return Info;
    }

    public static void removeInfoFromPrefs(Context context, String infoName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(infoName + "--weatherInfo");
        editor.remove(infoName + "--updateInfo");
        editor.apply();
    }
}
