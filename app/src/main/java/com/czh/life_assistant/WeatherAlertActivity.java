package com.czh.life_assistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.czh.life_assistant.adapter.WeatherAlertAdapter;
import com.czh.life_assistant.entity.AlertEntity;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.util.PrefsUtil;

import org.json.JSONException;

import java.util.ArrayList;

public class WeatherAlertActivity extends AppCompatActivity {

    private WeatherRootBean weatherRootBean;
    private String weatherInfo;

    private RecyclerView recyclerView;
    private WeatherAlertAdapter weatherAlertAdapter;
    private ArrayList<AlertEntity> alertList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alert);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String selectCity = PrefsUtil.getInfoFromPrefs(WeatherAlertActivity.this, "selectCity");
        weatherInfo = PrefsUtil.getInfoFromPrefs(WeatherAlertActivity.this, selectCity.split("--")[0] + "--weatherInfo");

        if (weatherInfo != null) {
            try {

                weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                int alertCount = weatherRootBean.getResult().getAlert().getContent().size();
                AlertEntity alertEntity;

                for (int i = 0; i < alertCount; i++) {
                    alertEntity = new AlertEntity();
                    alertEntity.setAlert_title(weatherRootBean.getResult().getAlert().getContent().get(i).getTitle());
                    alertEntity.setAlert_content(weatherRootBean.getResult().getAlert().getContent().get(i).getDescription());
                    alertEntity.setAlert_status(weatherRootBean.getResult().getAlert().getContent().get(i).getStatus());
                    alertList.add(alertEntity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAlertAdapter = new WeatherAlertAdapter(this, alertList);
        recyclerView.setAdapter(weatherAlertAdapter);

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
}
