package com.czh.life_assistant.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TransApi {

    public interface OnTransApiListener {
        void transStart();

        void transSuccess(String info);

        void transFailed();
    }

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public void getTransResult(String query, String from, String to, final OnTransApiListener listener) {

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        try {
            String URL_ENCODE_QUERY = URLEncoder.encode(query,"UTF-8");
            listener.transStart();
            OkHttpUtil.sendHttpRequest(TRANS_API_HOST + "?q=" + URL_ENCODE_QUERY + "&from=" + from + "&to=" + to + "&appid=" + appid + "&salt=" + salt + "&sign=" + MD5.md5(appid + query + salt + securityKey), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.transFailed();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    listener.transSuccess(response.body().string());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
