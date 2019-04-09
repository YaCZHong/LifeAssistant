package com.czh.life_assistant.entity.forfms;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by czh on 2018/5/4.
 */

public class FmsJsonParser {

    public static FmsRootBean getFmsInfo(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Gson gson = new Gson();
        FmsRootBean fmsRootBean = gson.fromJson(jsonObject.toString(), FmsRootBean.class);
        return fmsRootBean;
    }
}
