package com.czh.life_assistant.entity.forweather;

import com.google.gson.Gson;

import org.json.JSONException;

/**
 * Created by czh on 2018/5/4.
 */

public class WeatherJsonParser {

    public static WeatherRootBean getWeatherInfo(String json) throws JSONException {
        Gson gson = new Gson();
        return gson.fromJson(json, WeatherRootBean.class);
    }
}
