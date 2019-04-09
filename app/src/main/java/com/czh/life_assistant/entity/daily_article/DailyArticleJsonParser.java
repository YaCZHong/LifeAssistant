package com.czh.life_assistant.entity.daily_article;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by czh on 2018/5/4.
 */

public class DailyArticleJsonParser {

    public static DailyArticleRootBean getDailyArticleInfo(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(stripHtml(json));
        Gson gson = new Gson();
        DailyArticleRootBean dailyArticleRootBean = gson.fromJson(jsonObject.toString(), DailyArticleRootBean.class);
        return dailyArticleRootBean;
    }

    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p>", "<p>\t\t\t\t");
        return content;
    }

}
