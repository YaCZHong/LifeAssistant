package com.czh.life_assistant.entity.trans;

import com.czh.life_assistant.entity.forfms.FmsRootBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by czh on 2018/5/4.
 */

public class TransJsonParser {

    public static LanguageResult getTransInfo(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Gson gson = new Gson();
        LanguageResult result = gson.fromJson(jsonObject.toString(), LanguageResult.class);
        return result;
    }
}
