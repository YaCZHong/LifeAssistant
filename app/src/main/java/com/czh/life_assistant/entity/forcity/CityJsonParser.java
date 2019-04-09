package com.czh.life_assistant.entity.forcity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by czh on 2018/5/4.
 */

public class CityJsonParser {

    public static HeWeather6 getHeWeather6(String json)throws JSONException {
        JSONObject jsonObject=new JSONObject(json);
        JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
        Gson gson=new Gson();
        String citys = jsonArray.getJSONObject(0).toString();
        HeWeather6 heWeather6=gson.fromJson(citys,HeWeather6.class);

        return heWeather6;
    }
}
