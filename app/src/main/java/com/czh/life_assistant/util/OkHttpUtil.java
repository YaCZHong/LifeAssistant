package com.czh.life_assistant.util;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by czh on 2018/5/4.
 */

public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
            .build();

    public static void sendHttpRequest(String url, okhttp3.Callback callback) {

//        cancleAll(mOkHttpClient);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "application/x-java-serialized-object")
                .build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(callback);
    }

//    private static void cancleAll(OkHttpClient client) {
//        if (client == null) return;
//        for (Call call : client.dispatcher().queuedCalls()) {
//            call.cancel();
//        }
//        for (Call call : client.dispatcher().runningCalls()) {
//            call.cancel();
//        }
//    }

}
