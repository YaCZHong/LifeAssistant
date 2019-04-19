package com.czh.life_assistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.czh.life_assistant.adapter.WeatherDailyAdapter;
import com.czh.life_assistant.entity.DailyEntity;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.util.GetAQIqltyUtil;
import com.czh.life_assistant.util.GetImageUtil;
import com.czh.life_assistant.util.GetWeatherTxtUtil;
import com.czh.life_assistant.util.GetWind;
import com.czh.life_assistant.util.LunarCalendarUtils;
import com.czh.life_assistant.util.PrefsUtil;

import org.json.JSONException;

import java.util.ArrayList;

public class DailyWeatherActivity extends AppCompatActivity {

    private WeatherRootBean weatherRootBean;
    private WeatherDailyAdapter weatherDailyAdapter;
    private String weatherInfo;

    private RecyclerView recyclerView;
    private ArrayList<DailyEntity> dailyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String selectCity = PrefsUtil.getInfoFromPrefs(this, "selectCity");
        weatherInfo = PrefsUtil.getInfoFromPrefs(DailyWeatherActivity.this, selectCity.split("--")[0] + "--weatherInfo");

        if (weatherInfo != null) {
            try {

                weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);

                DailyEntity dailyEntity;

                for (int i = 0; i < 15; i++) {

                    dailyEntity = new DailyEntity();
                    dailyEntity.setImage1_Id(GetImageUtil.getImage(DailyWeatherActivity.this, weatherRootBean.getResult().getDaily().getSkycon_08h_20h().get(i).getValue()));
                    dailyEntity.setImage2_Id(GetImageUtil.getImage(DailyWeatherActivity.this, weatherRootBean.getResult().getDaily().getSkycon_20h_32h().get(i).getValue()));

                    int year = Integer.parseInt(weatherRootBean.getResult().getDaily().getSkycon().get(i).getDate().split("-")[0]);
                    int month = Integer.parseInt(weatherRootBean.getResult().getDaily().getSkycon().get(i).getDate().split("-")[1]);
                    int day = Integer.parseInt(weatherRootBean.getResult().getDaily().getSkycon().get(i).getDate().split("-")[2]);
                    LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(year, month, day));
                    String str_lunar = LunarCalendarUtils.getLunarDayString(year, month, day, lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, lunar.isLeap);

                    dailyEntity.setDaily_date(weatherRootBean.getResult().getDaily().getSkycon().get(i).getDate() + "  " + str_lunar);
                    dailyEntity.setDaily_temp((int) weatherRootBean.getResult().getDaily().getTemperature().get(i).getMax() + "° / " + (int) weatherRootBean.getResult().getDaily().getTemperature().get(i).getMin() + "°");
                    dailyEntity.setDaily_status(assembleDayAndNight(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getDaily().getSkycon_08h_20h().get(i).getValue()), GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getDaily().getSkycon_20h_32h().get(i).getValue())));
                    dailyEntity.setDaily_aqi((int) weatherRootBean.getResult().getDaily().getAqi().get(i).getAvg() + " " + GetAQIqltyUtil.getAqiQlty(weatherRootBean.getResult().getDaily().getAqi().get(i).getAvg()));
                    dailyEntity.setDaily_wind(GetWind.getWindDir(weatherRootBean.getResult().getDaily().getWind().get(i).getAvg().getDirection()) + " " + GetWind.getWindSc(weatherRootBean.getResult().getDaily().getWind().get(i).getAvg().getSpeed()));
                    dailyEntity.setDaily_hum(String.valueOf((int) (weatherRootBean.getResult().getDaily().getHumidity().get(i).getAvg() * 100)) + "%");
                    dailyEntity.setDaily_uv(weatherRootBean.getResult().getDaily().getUltraviolet().get(i).getDesc());
                    dailyEntity.setDaily_see(weatherRootBean.getResult().getDaily().getVisibility().get(i).getAvg() + "km");
                    dailyEntity.setDaily_comfort(weatherRootBean.getResult().getDaily().getComfort().get(i).getDesc());
                    dailyEntity.setDaily_sunrise(weatherRootBean.getResult().getDaily().getAstro().get(i).getSunrise().getTime());
                    dailyEntity.setDaily_sunset(weatherRootBean.getResult().getDaily().getAstro().get(i).getSunset().getTime());
                    dailyList.add(dailyEntity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView = findViewById(R.id.daily_weather_recyclerView);
        weatherDailyAdapter = new WeatherDailyAdapter(DailyWeatherActivity.this, R.layout.item_daily_list, dailyList);
        recyclerView.setAdapter(weatherDailyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_show);
        recyclerView.startAnimation(animation);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String assembleDayAndNight(String day_status, String night_status) {
        if (!day_status.equals(night_status)) {
            if (day_status.equals("晴天") && night_status.equals("晴夜")) {
                return "晴";
            } else {
                return day_status + "转" + night_status;
            }
        } else {
            return day_status;
        }
    }
}
