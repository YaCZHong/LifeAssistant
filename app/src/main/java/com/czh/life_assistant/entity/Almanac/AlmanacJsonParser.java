package com.czh.life_assistant.entity.Almanac;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by czh on 2018/5/4.
 */

public class AlmanacJsonParser {

    public static AlmanacRootBean getAlmanac(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Gson gson = new Gson();
        AlmanacRootBean almanacRootBean = gson.fromJson(jsonObject.toString(), AlmanacRootBean.class);
        return almanacRootBean;
    }
}
