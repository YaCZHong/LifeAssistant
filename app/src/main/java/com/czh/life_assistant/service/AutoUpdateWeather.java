package com.czh.life_assistant.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.czh.life_assistant.util.PrefsUtil;
import com.czh.life_assistant.util.RequestWeatherInfo;

public class AutoUpdateWeather extends Service implements RequestWeatherInfo.OnRequestWeatherListener {

    private IntentFilter intentFilter;
    private DateChangeReceiver dateChangeReceiver;

    private AlarmManager manager;
    private PendingIntent pi;

    private String[] arr_update_frequency = {"30分钟", "1小时", "2小时", "3小时", "4小时", "8小时"};
    private int frequency = 0;
    private int UPDATE_HOUR;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        dateChangeReceiver = new DateChangeReceiver();
        registerReceiver(dateChangeReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String selectCity = PrefsUtil.getInfoFromPrefs(getApplicationContext(), "selectCity");
        String update_frequency = PrefsUtil.getInfoFromPrefs(getApplicationContext(), "update_frequency");

        if (update_frequency != null) {
            for (int i = 0; i < 6; i++) {
                if (update_frequency.equals(arr_update_frequency[i])) {
                    frequency = i;
                    break;
                }
            }
        }

        switch (frequency) {
            case 0:
                UPDATE_HOUR = 1;
                break;
            case 1:
                UPDATE_HOUR = 2;
                break;
            case 2:
                UPDATE_HOUR = 4;
                break;
            case 3:
                UPDATE_HOUR = 6;
                break;
            case 4:
                UPDATE_HOUR = 8;
                break;
            case 5:
                UPDATE_HOUR = 16;
                break;
        }

        RequestWeatherInfo.RequestWeather(getApplicationContext(), selectCity.split("--")[0], selectCity.split("--")[1], this);

        /*
         * 定时任务
         * */
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int Hours = 30 * 60 * 1000 * UPDATE_HOUR;
        // 测试
//         int Hours = 60 * 1000 * UPDATE_HOUR;
        long triggerAtTime = SystemClock.elapsedRealtime() + Hours;
        Intent i = new Intent(this, AutoUpdateWeather.class);
        pi = PendingIntent.getService(this, 0, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void startRequestWeather() {
//        Log.d("AutoUpdateWeather", "startRequestWeather");
    }

    @Override
    public void requestWeatherSuccess(Context context, String cityName) {
        sendBroadcast(new Intent().setAction("com.czh.life_assistant.weatherUpdate"));
        RequestWeatherInfo.sendBroadcast(getApplicationContext());
    }

    @Override
    public void requestWeatherFailed() {
//        Log.d("AutoUpdateWeather", "requestWeatherFailed");
    }

    @Override
    public void onDestroy() {
        if (manager != null) {
            manager.cancel(pi);
        }
        super.onDestroy();
        unregisterReceiver(dateChangeReceiver);
    }

    class DateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            sendBroadcast(new Intent().setAction("com.czh.life_assistant.weatherUpdate"));
            RequestWeatherInfo.sendBroadcast(context);
           // Log.d("AutoUpdate","onReceiver...");
        }
    }

}
