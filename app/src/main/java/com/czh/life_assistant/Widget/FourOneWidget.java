package com.czh.life_assistant.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.RemoteViews;

import com.czh.life_assistant.MainActivity;
import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.util.GetImageUtil;
import com.czh.life_assistant.util.GetWeatherTxtUtil;
import com.czh.life_assistant.util.LunarCalendarUtils;
import com.czh.life_assistant.util.PrefsUtil;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class FourOneWidget extends AppWidgetProvider {

    // 更新 widget 的广播对应的action
    private String MYWIDGET_UPDATE = "com.czh.life_assistant.action.MYWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(MYWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = getRemoteViews(context);
            if (views != null) {
                appWidgetManager.updateAppWidget(new ComponentName(context, FourOneWidget.class), views);
            }
        }
    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = getRemoteViews(context);
        if (views != null) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    private RemoteViews getRemoteViews(Context context) {

        WeatherRootBean weatherRootBean;
        String selectCity = PrefsUtil.getInfoFromPrefs(context, "selectCity");
        if (selectCity != null) {
            //String updateTime = PrefsUtil.getInfoFromPrefs(context, selectCity.split("--")[0] + "--updateInfo");
            String weatherInfo = PrefsUtil.getInfoFromPrefs(context, selectCity.split("--")[0] + "--weatherInfo");
            if (weatherInfo != null) {
                try {
                    weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_four_one);

                    views.setViewVisibility(R.id.widget_four_one_hint, View.GONE);
                    views.setViewVisibility(R.id.widget_four_one_clock, View.VISIBLE);
                    views.setViewVisibility(R.id.widget_four_one_clock_aa, View.VISIBLE);
                    views.setViewVisibility(R.id.widget_four_one_date, View.VISIBLE);
                    views.setViewVisibility(R.id.widget_four_one_lunar, View.VISIBLE);
                    views.setViewVisibility(R.id.widget_four_one_temp, View.VISIBLE);
                    views.setViewVisibility(R.id.widget_four_one_city, View.VISIBLE);
                    views.setViewVisibility(R.id.widget_four_one_icon, View.VISIBLE);

                    Calendar c = Calendar.getInstance();
                    LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));
                    //Log.d("lunar", "lunar: " + lunar.lunarYear + "--" + lunar.lunarMonth + "--" + lunar.lunarDay);
                    String str_lunar = LunarCalendarUtils.getLunarDayString(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);
                    //Log.d("lunar", "lunar: " + str_lunar);

                    views.setTextViewText(R.id.widget_four_one_lunar, " | " + str_lunar);
                    views.setTextViewText(R.id.widget_four_one_temp, String.valueOf((int) weatherRootBean.getResult().getRealtime().getTemperature()) + "℃");
                    views.setTextViewText(R.id.widget_four_one_city, GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getRealtime().getSkycon()) + "  " + selectCity.split("--")[0]);
                    String widget_style = PrefsUtil.getInfoFromPrefs(context, "widget_color");
                    if ((widget_style != null) && (widget_style.equals("暗色"))) {

                        views.setTextColor(R.id.widget_four_one_clock, Color.parseColor("#424242"));
                        views.setTextColor(R.id.widget_four_one_clock_aa, Color.parseColor("#424242"));
                        views.setTextColor(R.id.widget_four_one_date, Color.parseColor("#424242"));
                        views.setTextColor(R.id.widget_four_one_lunar, Color.parseColor("#424242"));
                        views.setTextColor(R.id.widget_four_one_temp, Color.parseColor("#424242"));
                        views.setTextColor(R.id.widget_four_one_city, Color.parseColor("#424242"));
                    } else {

                        views.setTextColor(R.id.widget_four_one_clock, Color.parseColor("#ffffff"));
                        views.setTextColor(R.id.widget_four_one_clock_aa, Color.parseColor("#ffffff"));
                        views.setTextColor(R.id.widget_four_one_date, Color.parseColor("#ffffff"));
                        views.setTextColor(R.id.widget_four_one_lunar, Color.parseColor("#ffffff"));
                        views.setTextColor(R.id.widget_four_one_temp, Color.parseColor("#ffffff"));
                        views.setTextColor(R.id.widget_four_one_city, Color.parseColor("#ffffff"));
                    }
                    views.setImageViewResource(R.id.widget_four_one_icon, GetImageUtil.getImage(context, weatherRootBean.getResult().getRealtime().getSkycon()));
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
                    views.setOnClickPendingIntent(R.id.widget_four_one_temp, pendingIntent1);
                    views.setOnClickPendingIntent(R.id.widget_four_one_icon, pendingIntent1);
                    views.setOnClickPendingIntent(R.id.widget_four_one_city, pendingIntent1);

                    Intent intentClock = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                    PendingIntent pendingIntentClock = PendingIntent.getActivity(context, 0, intentClock, 0);
                    views.setOnClickPendingIntent(R.id.widget_four_one_clock, pendingIntentClock);

                    Intent intent = new Intent();
                    ComponentName cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
                    intent.setComponent(cn);
                    PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent, 0);
                    views.setOnClickPendingIntent(R.id.widget_four_one_date, pendingIntent2);
                    views.setOnClickPendingIntent(R.id.widget_four_one_lunar, pendingIntent2);
                    return views;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_four_one);

            views.setViewVisibility(R.id.widget_four_one_hint, View.VISIBLE);
            views.setViewVisibility(R.id.widget_four_one_clock, View.GONE);
            views.setViewVisibility(R.id.widget_four_one_clock_aa, View.GONE);
            views.setViewVisibility(R.id.widget_four_one_date, View.GONE);
            views.setViewVisibility(R.id.widget_four_one_lunar, View.GONE);
            views.setViewVisibility(R.id.widget_four_one_temp, View.GONE);
            views.setViewVisibility(R.id.widget_four_one_city, View.GONE);
            views.setViewVisibility(R.id.widget_four_one_icon, View.GONE);

            String widget_style = PrefsUtil.getInfoFromPrefs(context, "widget_color");
            if ((widget_style != null) && (widget_style.equals("暗色"))) {
                views.setTextColor(R.id.widget_four_one_hint, Color.parseColor("#424242"));
            } else {
                views.setTextColor(R.id.widget_four_one_hint, Color.parseColor("#ffffff"));
            }
            return views;
        }
        return null;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}

