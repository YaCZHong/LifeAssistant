package com.czh.life_assistant.entity.news;

import com.czh.life_assistant.entity.Almanac.AlmanacRootBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by czh on 2018/5/4.
 */

public class NewsJsonParser {

    public static NewsRootBean getNews(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Gson gson = new Gson();
        NewsRootBean news = gson.fromJson(jsonObject.toString(), NewsRootBean.class);
        return news;
    }
}
