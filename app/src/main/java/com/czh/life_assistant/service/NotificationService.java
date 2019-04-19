package com.czh.life_assistant.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.czh.life_assistant.MainActivity;
import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.util.GetImageUtil;
import com.czh.life_assistant.util.GetWeatherTxtUtil;
import com.czh.life_assistant.util.GetWeekday;
import com.czh.life_assistant.util.PrefsUtil;
import com.czh.life_assistant.util.RequestWeatherInfo;

import org.json.JSONException;

import java.util.Calendar;


public class NotificationService extends Service implements RequestWeatherInfo.OnRequestWeatherListener {

    private AlarmManager alarmManager;
    private NotificationManager notificationManager;
    private PendingIntent pi;
    private String selectCity;
    private String[] arr_notify_type = {"普通", "详细"};

    private IntentFilter intentFilter;
    private WeatherChangeReceiver weatherChangeReceiver;

    private enum Result {
        RESULT_SUCCESS, RESULT_FAILED
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.czh.life_assistant.weatherUpdate");
        weatherChangeReceiver = new WeatherChangeReceiver();
        registerReceiver(weatherChangeReceiver, intentFilter);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "MY_CHANNEL_AUTO_UPDATE";
            String name = "通知服务";
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
            builder.setChannelId(id);
        }
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_sample);

        Intent toMain = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, toMain, 0))
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
                .setSmallIcon(R.drawable.ic_default_na_w)
                .setGroupSummary(false)
                .setGroup("group")
                .setWhen(System.currentTimeMillis());

        notification = builder.build();
        startForeground(1, notification);

        selectCity = PrefsUtil.getInfoFromPrefs(getApplicationContext(), "selectCity");
        RequestWeatherInfo.RequestWeather(getApplicationContext(), selectCity.split("--")[0], selectCity.split("--")[1], this);

        /*
         * 定时任务
         * */
        int Hours = 30 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + Hours;
        Intent i = new Intent(this, NotificationService.class);
        pi = PendingIntent.getService(this, 0, i, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void startRequestWeather() {

    }

    @Override
    public void requestWeatherSuccess(Context context, String cityName) {
        sendBroadcast(new Intent().setAction("com.czh.life_assistant.weatherUpdate"));
    }

    @Override
    public void requestWeatherFailed() {
        updateNotification(Result.RESULT_FAILED);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        if (alarmManager != null) {
            alarmManager.cancel(pi);
        }
        super.onDestroy();
        unregisterReceiver(weatherChangeReceiver);
    }


    //更新Notification的方法
    public void updateNotification(Result result) {
        if (result == Result.RESULT_SUCCESS) {
            WeatherRootBean weatherRootBean = getWeatherInfo();
            if (weatherRootBean != null) {
                updateRemoteView(weatherRootBean);
            }
        } else {
            updateRemoteView(null);
        }
    }

    private WeatherRootBean getWeatherInfo() {
        WeatherRootBean weatherRootBean = null;
        String selectCity = PrefsUtil.getInfoFromPrefs(getApplicationContext(), "selectCity");
        if (selectCity != null) {
            String weatherInfo = PrefsUtil.getInfoFromPrefs(getApplicationContext(), selectCity.split("--")[0] + "--weatherInfo");
            if (weatherInfo != null) {
                try {
                    weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return weatherRootBean;
    }

    private void updateRemoteView(WeatherRootBean weatherRootBean) {
        Notification notification;
        RemoteViews remoteViews = null;
        Intent toMain = new Intent(this, MainActivity.class);
        String notification_style = PrefsUtil.getInfoFromPrefs(getApplicationContext(), "notification_style");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "MY_CHANNEL_AUTO_UPDATE";
            String name = "通知服务";

            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
            builder.setChannelId(id);
        }

        if (weatherRootBean != null) {
            if (notification_style != null && notification_style.equals(arr_notify_type[1])) {
                remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_detail);

                remoteViews.setViewVisibility(R.id.tv_hint, View.GONE);
                remoteViews.setViewVisibility(R.id.now_info, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.daily_info, View.VISIBLE);
                remoteViews.setImageViewResource(R.id.notification_detail_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getRealtime().getSkycon()));
                remoteViews.setImageViewResource(R.id.daily_info_1_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getDaily().getSkycon().get(0).getValue()));
                remoteViews.setImageViewResource(R.id.daily_info_2_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getDaily().getSkycon().get(1).getValue()));
                remoteViews.setImageViewResource(R.id.daily_info_3_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getDaily().getSkycon().get(2).getValue()));
                remoteViews.setImageViewResource(R.id.daily_info_4_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getDaily().getSkycon().get(3).getValue()));
                remoteViews.setImageViewResource(R.id.daily_info_5_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getDaily().getSkycon().get(4).getValue()));

                remoteViews.setTextViewText(R.id.notification_detail_temp, String.valueOf((int) (weatherRootBean.getResult().getRealtime().getTemperature())) + "°");
                remoteViews.setTextViewText(R.id.notification_detail_temp_aqi, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin())) + " / " + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(0).getMax())) + "°    AQI  " + (int) (weatherRootBean.getResult().getRealtime().getAqi()));
                remoteViews.setTextViewText(R.id.notification_detail_status, GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getRealtime().getSkycon()));
                remoteViews.setTextViewText(R.id.notification_detail_city, selectCity.split("--")[0]);
                remoteViews.setTextViewText(R.id.notification_detail_time, PrefsUtil.getInfoFromPrefs(getApplicationContext(), selectCity.split("--")[0] + "--updateInfo"));

                //得到当天是星期几
                Calendar c = Calendar.getInstance();
                int num = c.get(Calendar.DAY_OF_WEEK);

                remoteViews.setTextViewText(R.id.daily_info_1, GetWeekday.getWeekdayZh(num++));
                remoteViews.setTextViewText(R.id.daily_info_2, GetWeekday.getWeekdayZh(num++));
                remoteViews.setTextViewText(R.id.daily_info_3, GetWeekday.getWeekdayZh(num++));
                remoteViews.setTextViewText(R.id.daily_info_4, GetWeekday.getWeekdayZh(num++));
                remoteViews.setTextViewText(R.id.daily_info_5, GetWeekday.getWeekdayZh(num));

                remoteViews.setTextViewText(R.id.daily_info_1_temp, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin())) + "/" + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(0).getMax())) + "°");
                remoteViews.setTextViewText(R.id.daily_info_2_temp, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(1).getMin())) + "/" + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(1).getMax())) + "°");
                remoteViews.setTextViewText(R.id.daily_info_3_temp, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(2).getMin())) + "/" + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(2).getMax())) + "°");
                remoteViews.setTextViewText(R.id.daily_info_4_temp, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(3).getMin())) + "/" + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(3).getMax())) + "°");
                remoteViews.setTextViewText(R.id.daily_info_5_temp, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(4).getMin())) + "/" + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(4).getMax())) + "°");
            } else {
                remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_sample);

                remoteViews.setViewVisibility(R.id.tv_hint, View.GONE);
                remoteViews.setViewVisibility(R.id.ll_weatherInfo, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.ll_otherInfo, View.VISIBLE);
                remoteViews.setImageViewResource(R.id.notification_simple_iv, GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getRealtime().getSkycon()));
                remoteViews.setTextViewText(R.id.notification_simple_temp, String.valueOf((int) (weatherRootBean.getResult().getRealtime().getTemperature())) + "°");
                remoteViews.setTextViewText(R.id.notification_simple_temp_aqi, String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin())) + " / " + String.valueOf((int) (weatherRootBean.getResult().getDaily().getTemperature().get(0).getMax())) + "°    AQI  " + (int) (weatherRootBean.getResult().getRealtime().getAqi()));
                remoteViews.setTextViewText(R.id.notification_simple_status, GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getRealtime().getSkycon()));
                remoteViews.setTextViewText(R.id.notification_simple_city, selectCity.split("--")[0]);
                remoteViews.setTextViewText(R.id.notification_simple_time, PrefsUtil.getInfoFromPrefs(getApplicationContext(), selectCity.split("--")[0] + "--updateInfo"));
            }

            builder.setContentIntent(PendingIntent.getActivity(this, 0, toMain, 0))
                    .setOngoing(true)
                    .setCustomContentView(remoteViews)
                    .setCustomBigContentView(remoteViews)
                    .setSmallIcon(GetImageUtil.getImage(getApplicationContext(), weatherRootBean.getResult().getRealtime().getSkycon()))
                    .setGroupSummary(false)
                    .setGroup("group")
                    .setWhen(System.currentTimeMillis());

        } else {
            if (notification_style != null && notification_style.equals(arr_notify_type[1])) {
                remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_detail);
                remoteViews.setViewVisibility(R.id.tv_hint, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.now_info, View.GONE);
                remoteViews.setViewVisibility(R.id.daily_info, View.GONE);
                remoteViews.setTextViewText(R.id.tv_hint, "加载失败...");
            } else {
                remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_sample);
                remoteViews.setViewVisibility(R.id.tv_hint, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.ll_weatherInfo, View.GONE);
                remoteViews.setViewVisibility(R.id.ll_otherInfo, View.GONE);
                remoteViews.setTextViewText(R.id.tv_hint, "加载失败...");
            }

            builder.setContentIntent(PendingIntent.getActivity(this, 0, toMain, 0))
                    .setOngoing(true)
                    .setCustomContentView(remoteViews)
                    .setCustomBigContentView(remoteViews)
                    .setSmallIcon(R.drawable.ic_default_na_w)
                    .setGroupSummary(false)
                    .setGroup("group")
                    .setWhen(System.currentTimeMillis());
        }

        notification = builder.build();
        notificationManager.notify(1, notification);
    }

    class WeatherChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("NotificationService:","Receice...");

            WeatherRootBean weatherRootBean = getWeatherInfo();
            if (weatherRootBean != null) {
                updateRemoteView(weatherRootBean);
            }
        }
    }
}
