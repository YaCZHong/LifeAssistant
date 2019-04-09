package com.czh.life_assistant.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;

public class RequestWeatherInfo {

    public interface OnRequestWeatherListener {

        void startRequestWeather();

        void requestWeatherSuccess(Context context, String cityName);

        void requestWeatherFailed();

    }

    public static void RequestWeather(final Context context, final String cityInfo, final String lat_lon, final OnRequestWeatherListener listener) {

        listener.startRequestWeather();

        String request_weather_url = "https://api.caiyunapp.com/v2/BM=Ue5yHxHP81BIn/" + lat_lon + "/weather?alert=true&dailysteps=360";

        OkHttpUtil.sendHttpRequest(request_weather_url, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String weatherInfo = response.body().string();
                        //Log.d("RequestWeatherInfo", "onResponse: " + weatherInfo);
                        //Log.d("RequestWeatherInfo", "onResponse: " + cityInfo);
                        PrefsUtil.saveInfoToPrefs(context, cityInfo + "--weatherInfo", weatherInfo);

                        String updateInfo = GetTimeUtil.getCurrentTime();
                        PrefsUtil.saveInfoToPrefs(context, cityInfo + "--updateInfo", updateInfo);
                        listener.requestWeatherSuccess(context, cityInfo);

//                        sendBroadcast(context);
//                        showNotification(context);

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (call.isCanceled()) {
                            listener.requestWeatherFailed();
                            //Log.d("RequestWeather", "cancel");
                        } else {
                            listener.requestWeatherFailed();
                        }
                    }
                }
        );
    }

    /*
     * 发送更新小部件广播
     * */
    public static void sendBroadcast(Context context) {
        Intent intent = new Intent("com.czh.weather.action.MYWIDGET_UPDATE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        } else {
            context.sendBroadcast(intent);
        }
        //Log.d("sendBroadcast", "sendBroadcast");
    }

    public static void showNotification(Context context) {
        String isNotify = PrefsUtil.getInfoFromPrefs(context, "isNotify");
        if (isNotify != null) {
            if (isNotify.equals("YES")) {
                //Log.d("showNotification", "showNotification");
                //NotificationUtil.createNotification(context);
            }
        }
    }
}
