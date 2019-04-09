package com.czh.life_assistant.util;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RequestDailyArticleInfo {

    public interface OnRequestDailyArticleListener {

        void startRequestDailyArticle();

        void requestDailyArticleSuccess(String articleInfo);

        void requestDailyArticleFailed();
    }

    public static void requestDailyArticle(String date, final OnRequestDailyArticleListener listener) {

        listener.startRequestDailyArticle();

        String url;

        if (date != null) {

            if(date.equals("00000000")){
                url = "https://interface.meiriyiwen.com/article/random?dev=1";
            }else {
                //https://interface.meiriyiwen.com/article/day?dev=1&date=20170216
                url = "https://interface.meiriyiwen.com/article/day?dev=1&date=" + date;
            }
        } else {
            url = "https://interface.meiriyiwen.com/article/today?dev=1";
        }

        OkHttpUtil.sendHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.requestDailyArticleFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String articleInfo = response.body().string();
                listener.requestDailyArticleSuccess(articleInfo);
            }
        });

    }
}
