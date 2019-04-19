package com.czh.life_assistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.DailyWeatherActivity;
import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.util.GetAQIqltyUtil;
import com.czh.life_assistant.util.GetImageUtil;
import com.czh.life_assistant.util.GetWeatherTxtUtil;
import com.czh.life_assistant.util.GetWeekday;
import com.czh.life_assistant.util.GetWind;
import com.czh.life_assistant.util.PrefsUtil;
import com.czh.life_assistant.util.RequestWeatherInfo;
import com.czh.life_assistant.view.LineChartView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class WeatherFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollView;
    private ImageView imageView;
    private Context mContext;
    private WeatherRootBean weatherRootBean;

    /*
     * real
     * */
    private ImageView weather_real_status_icon;
    private TextView weather_temp_now;
    private TextView weather_real_description;
    private TextView weather_update_time;

    /*
     * daily
     * */
    private TextView day_1;
    private TextView day_2;
    private TextView day_3;
    private TextView day_4;
    private TextView day_5;

    private ImageView day_1_iv;
    private ImageView day_2_iv;
    private ImageView day_3_iv;
    private ImageView day_4_iv;
    private ImageView day_5_iv;

    private TextView day_1_temp_max;
    private TextView day_2_temp_max;
    private TextView day_3_temp_max;
    private TextView day_4_temp_max;
    private TextView day_5_temp_max;

    private TextView day_1_temp_min;
    private TextView day_2_temp_min;
    private TextView day_3_temp_min;
    private TextView day_4_temp_min;
    private TextView day_5_temp_min;

    private RelativeLayout rl_dailyList;

    /*
     * hourly
     * */

    private TextView weather_status_hourly_1;
    private TextView weather_status_hourly_2;
    private TextView weather_status_hourly_3;
    private TextView weather_status_hourly_4;
    private TextView weather_status_hourly_5;
    private TextView weather_status_hourly_6;
    private TextView weather_status_hourly_7;
    private TextView weather_status_hourly_8;
    private TextView weather_status_hourly_9;
    private TextView weather_status_hourly_10;
    private TextView weather_status_hourly_11;
    private TextView weather_status_hourly_12;
    private TextView weather_status_hourly_13;
    private TextView weather_status_hourly_14;
    private TextView weather_status_hourly_15;
    private TextView weather_status_hourly_16;
    private TextView weather_status_hourly_17;
    private TextView weather_status_hourly_18;
    private TextView weather_status_hourly_19;
    private TextView weather_status_hourly_20;
    private TextView weather_status_hourly_21;
    private TextView weather_status_hourly_22;
    private TextView weather_status_hourly_23;
    private TextView weather_status_hourly_24;

    private ImageView weather_icon_hourly_1;
    private ImageView weather_icon_hourly_2;
    private ImageView weather_icon_hourly_3;
    private ImageView weather_icon_hourly_4;
    private ImageView weather_icon_hourly_5;
    private ImageView weather_icon_hourly_6;
    private ImageView weather_icon_hourly_7;
    private ImageView weather_icon_hourly_8;
    private ImageView weather_icon_hourly_9;
    private ImageView weather_icon_hourly_10;
    private ImageView weather_icon_hourly_11;
    private ImageView weather_icon_hourly_12;
    private ImageView weather_icon_hourly_13;
    private ImageView weather_icon_hourly_14;
    private ImageView weather_icon_hourly_15;
    private ImageView weather_icon_hourly_16;
    private ImageView weather_icon_hourly_17;
    private ImageView weather_icon_hourly_18;
    private ImageView weather_icon_hourly_19;
    private ImageView weather_icon_hourly_20;
    private ImageView weather_icon_hourly_21;
    private ImageView weather_icon_hourly_22;
    private ImageView weather_icon_hourly_23;
    private ImageView weather_icon_hourly_24;

    private LineChartView lineChartView;


    /*
     * other
     * */
    private TextView weather_wind;
    private TextView weather_wind_sp;
    private TextView weather_hum;
    private TextView weather_aqi;
    private TextView weather_aqi_quality;
    private TextView weather_sunrise;
    private TextView weather_sunset;
    private TextView weather_uv;
    private TextView weather_comfort;
    private TextView weather_pm25;
    private TextView weather_pm10;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//设置刷新监听器
            @Override
            public void onRefresh() {

                final String selectCity = PrefsUtil.getInfoFromPrefs(mContext, "selectCity");

                if (selectCity != null) {
                    RequestWeatherInfo.RequestWeather(mContext, selectCity.split("--")[0], selectCity.split("--")[1], new RequestWeatherInfo.OnRequestWeatherListener() {
                        @Override
                        public void startRequestWeather() {
                        }

                        @Override
                        public void requestWeatherSuccess(Context context, String cityInfo) {
                            setSwipeRefreshLayoutVisable(false);
                            String weatherInfo = PrefsUtil.getInfoFromPrefs(getActivity(), cityInfo + "--weatherInfo");
                            if (weatherInfo != null) {
                                try {
                                    weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                                    showData(weatherRootBean, cityInfo);
                                    showToast("获取数据成功");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void requestWeatherFailed() {
                            setSwipeRefreshLayoutVisable(false);
                            showToast("刷新失败，请检查网络");
                        }
                    });
                } else {
                    setSwipeRefreshLayoutVisable(false);
                    Toast.makeText(mContext, "请选择城市后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });

        scrollView = view.findViewById(R.id.scrollview);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
                }
            }
        });

        imageView = view.findViewById(R.id.weather_bg);
        Picasso.with(mContext).load(R.drawable.weather_bg).into(imageView);

        initView(view);

        String selectCity = PrefsUtil.getInfoFromPrefs(mContext, "selectCity");
        if (selectCity != null) {
            String weatherInfo = PrefsUtil.getInfoFromPrefs(getActivity(), selectCity.split("--")[0] + "--weatherInfo");

            if (weatherInfo != null) {
                try {

                    weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                    showData(weatherRootBean, selectCity);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showNoData();
        }

        return view;
    }


    private void initView(View view) {

        /*
         * real
         * */
        weather_temp_now = view.findViewById(R.id.weather_real_temp);
        weather_real_status_icon = view.findViewById(R.id.weather_real_status_icon);
        weather_real_description = view.findViewById(R.id.weather_real_description);
        weather_update_time = view.findViewById(R.id.weather_update_time);

        /*
         * daily
         * */
        day_1 = view.findViewById(R.id.day_1);
        day_2 = view.findViewById(R.id.day_2);
        day_3 = view.findViewById(R.id.day_3);
        day_4 = view.findViewById(R.id.day_4);
        day_5 = view.findViewById(R.id.day_5);

        day_1_iv = view.findViewById(R.id.day_1_iv);
        day_2_iv = view.findViewById(R.id.day_2_iv);
        day_3_iv = view.findViewById(R.id.day_3_iv);
        day_4_iv = view.findViewById(R.id.day_4_iv);
        day_5_iv = view.findViewById(R.id.day_5_iv);

        day_1_temp_max = view.findViewById(R.id.day_1_temp_max);
        day_2_temp_max = view.findViewById(R.id.day_2_temp_max);
        day_3_temp_max = view.findViewById(R.id.day_3_temp_max);
        day_4_temp_max = view.findViewById(R.id.day_4_temp_max);
        day_5_temp_max = view.findViewById(R.id.day_5_temp_max);

        day_1_temp_min = view.findViewById(R.id.day_1_temp_min);
        day_2_temp_min = view.findViewById(R.id.day_2_temp_min);
        day_3_temp_min = view.findViewById(R.id.day_3_temp_min);
        day_4_temp_min = view.findViewById(R.id.day_4_temp_min);
        day_5_temp_min = view.findViewById(R.id.day_5_temp_min);

        rl_dailyList = view.findViewById(R.id.rl_dailyList);
        rl_dailyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectCity = PrefsUtil.getInfoFromPrefs(mContext, "selectCity");
                if(selectCity!=null){
                    Intent intent = new Intent(getActivity(), DailyWeatherActivity.class);
                    startActivity(intent);
                }else {
                    showToast("暂未选择城市");
                }

            }
        });

        /*
         * hourly
         * */
        weather_status_hourly_1 = view.findViewById(R.id.hour_1_tv);
        weather_status_hourly_2 = view.findViewById(R.id.hour_2_tv);
        weather_status_hourly_3 = view.findViewById(R.id.hour_3_tv);
        weather_status_hourly_4 = view.findViewById(R.id.hour_4_tv);
        weather_status_hourly_5 = view.findViewById(R.id.hour_5_tv);
        weather_status_hourly_6 = view.findViewById(R.id.hour_6_tv);
        weather_status_hourly_7 = view.findViewById(R.id.hour_7_tv);
        weather_status_hourly_8 = view.findViewById(R.id.hour_8_tv);
        weather_status_hourly_9 = view.findViewById(R.id.hour_9_tv);
        weather_status_hourly_10 = view.findViewById(R.id.hour_10_tv);
        weather_status_hourly_11 = view.findViewById(R.id.hour_11_tv);
        weather_status_hourly_12 = view.findViewById(R.id.hour_12_tv);
        weather_status_hourly_13 = view.findViewById(R.id.hour_13_tv);
        weather_status_hourly_14 = view.findViewById(R.id.hour_14_tv);
        weather_status_hourly_15 = view.findViewById(R.id.hour_15_tv);
        weather_status_hourly_16 = view.findViewById(R.id.hour_16_tv);
        weather_status_hourly_17 = view.findViewById(R.id.hour_17_tv);
        weather_status_hourly_18 = view.findViewById(R.id.hour_18_tv);
        weather_status_hourly_19 = view.findViewById(R.id.hour_19_tv);
        weather_status_hourly_20 = view.findViewById(R.id.hour_20_tv);
        weather_status_hourly_21 = view.findViewById(R.id.hour_21_tv);
        weather_status_hourly_22 = view.findViewById(R.id.hour_22_tv);
        weather_status_hourly_23 = view.findViewById(R.id.hour_23_tv);
        weather_status_hourly_24 = view.findViewById(R.id.hour_24_tv);

        weather_icon_hourly_1 = view.findViewById(R.id.hour_1_iv);
        weather_icon_hourly_2 = view.findViewById(R.id.hour_2_iv);
        weather_icon_hourly_3 = view.findViewById(R.id.hour_3_iv);
        weather_icon_hourly_4 = view.findViewById(R.id.hour_4_iv);
        weather_icon_hourly_5 = view.findViewById(R.id.hour_5_iv);
        weather_icon_hourly_6 = view.findViewById(R.id.hour_6_iv);
        weather_icon_hourly_7 = view.findViewById(R.id.hour_7_iv);
        weather_icon_hourly_8 = view.findViewById(R.id.hour_8_iv);
        weather_icon_hourly_9 = view.findViewById(R.id.hour_9_iv);
        weather_icon_hourly_10 = view.findViewById(R.id.hour_10_iv);
        weather_icon_hourly_11 = view.findViewById(R.id.hour_11_iv);
        weather_icon_hourly_12 = view.findViewById(R.id.hour_12_iv);
        weather_icon_hourly_13 = view.findViewById(R.id.hour_13_iv);
        weather_icon_hourly_14 = view.findViewById(R.id.hour_14_iv);
        weather_icon_hourly_15 = view.findViewById(R.id.hour_15_iv);
        weather_icon_hourly_16 = view.findViewById(R.id.hour_16_iv);
        weather_icon_hourly_17 = view.findViewById(R.id.hour_17_iv);
        weather_icon_hourly_18 = view.findViewById(R.id.hour_18_iv);
        weather_icon_hourly_19 = view.findViewById(R.id.hour_19_iv);
        weather_icon_hourly_20 = view.findViewById(R.id.hour_20_iv);
        weather_icon_hourly_21 = view.findViewById(R.id.hour_21_iv);
        weather_icon_hourly_22 = view.findViewById(R.id.hour_22_iv);
        weather_icon_hourly_23 = view.findViewById(R.id.hour_23_iv);
        weather_icon_hourly_24 = view.findViewById(R.id.hour_24_iv);

        lineChartView = view.findViewById(R.id.LineChartView_hourly);

        /*
         * other
         * */

        weather_wind = view.findViewById(R.id.wind);
        weather_wind_sp = view.findViewById(R.id.wind_sp);
        weather_hum = view.findViewById(R.id.hum);
        weather_aqi = view.findViewById(R.id.aqi);
        weather_aqi_quality = view.findViewById(R.id.aqi_quality);
        weather_sunrise = view.findViewById(R.id.sunrise);
        weather_sunset = view.findViewById(R.id.sunset);
        weather_uv = view.findViewById(R.id.uv);
        weather_comfort = view.findViewById(R.id.comfort);
        weather_pm25 = view.findViewById(R.id.pm25);
        weather_pm10 = view.findViewById(R.id.pm10);

    }

    private void showNoData() {

        //得到星期几
        Calendar c = Calendar.getInstance();
        int num = c.get(Calendar.DAY_OF_WEEK);

        day_1.setText(GetWeekday.getWeekdayZh(num++));
        day_2.setText(GetWeekday.getWeekdayZh(num++));
        day_3.setText(GetWeekday.getWeekdayZh(num++));
        day_4.setText(GetWeekday.getWeekdayZh(num++));
        day_5.setText(GetWeekday.getWeekdayZh(num));
    }

    public void showData(final WeatherRootBean weatherRootBean, final String SelectCity) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String updateInfo = PrefsUtil.getInfoFromPrefs(getActivity(), SelectCity.split("--")[0] + "--updateInfo");
                if (updateInfo != null) {
                    weather_update_time.setText(updateInfo);
                }
                weather_real_description.setText(weatherRootBean.getResult().getForecast_keypoint());
                weather_real_status_icon.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getRealtime().getSkycon()));
                weather_temp_now.setText(SelectCity.split("--")[0] + "  " + String.valueOf((int) weatherRootBean.getResult().getRealtime().getTemperature()) + "°");

                weather_wind.setText(GetWind.getWindDir(weatherRootBean.getResult().getRealtime().getWind().getDirection()));
                weather_wind_sp.setText(GetWind.getWindSc(weatherRootBean.getResult().getRealtime().getWind().getSpeed()));
                weather_hum.setText(String.valueOf((int) (weatherRootBean.getResult().getRealtime().getHumidity() * 100)) + "%");
                weather_aqi.setText((int) weatherRootBean.getResult().getRealtime().getAqi() + "");
                weather_aqi_quality.setText(GetAQIqltyUtil.getAqiQlty(weatherRootBean.getResult().getRealtime().getAqi()));
                weather_sunrise.setText(weatherRootBean.getResult().getDaily().getAstro().get(0).getSunrise().getTime());
                weather_sunset.setText(weatherRootBean.getResult().getDaily().getAstro().get(0).getSunset().getTime());
                weather_uv.setText(weatherRootBean.getResult().getRealtime().getUltraviolet().getDesc());
                weather_comfort.setText(weatherRootBean.getResult().getRealtime().getComfort().getDesc());
                weather_pm25.setText(weatherRootBean.getResult().getRealtime().getPm25() + "μg/m3");
                weather_pm10.setText(weatherRootBean.getResult().getRealtime().getPm10() + "μg/m3");

                /*
                 * daily数据
                 * */
                //得到星期几
                Calendar c = Calendar.getInstance();
                String date[] = weatherRootBean.getResult().getDaily().getSkycon().get(0).getDate().split("-");
                c.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
                int num = c.get(Calendar.DAY_OF_WEEK);

                day_1.setText(GetWeekday.getWeekday(num++));
                day_2.setText(GetWeekday.getWeekday(num++));
                day_3.setText(GetWeekday.getWeekday(num++));
                day_4.setText(GetWeekday.getWeekday(num++));
                day_5.setText(GetWeekday.getWeekday(num));

                day_1_temp_max.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(0).getMax()) + "°/");
                day_2_temp_max.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(1).getMax()) + "°/");
                day_3_temp_max.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(2).getMax()) + "°/");
                day_4_temp_max.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(3).getMax()) + "°/");
                day_5_temp_max.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(4).getMax()) + "°/");

                day_1_temp_min.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin()) + "°");
                day_2_temp_min.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin()) + "°");
                day_3_temp_min.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin()) + "°");
                day_4_temp_min.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin()) + "°");
                day_5_temp_min.setText(String.valueOf((int) weatherRootBean.getResult().getDaily().getTemperature().get(0).getMin()) + "°");

                day_1_iv.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getDaily().getSkycon().get(0).getValue()));
                day_2_iv.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getDaily().getSkycon().get(1).getValue()));
                day_3_iv.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getDaily().getSkycon().get(2).getValue()));
                day_4_iv.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getDaily().getSkycon().get(3).getValue()));
                day_5_iv.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getDaily().getSkycon().get(4).getValue()));

                /*
                 * hourly数据
                 * */
                weather_status_hourly_1.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(0).getValue()));
                weather_status_hourly_2.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(1).getValue()));
                weather_status_hourly_3.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(2).getValue()));
                weather_status_hourly_4.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(3).getValue()));
                weather_status_hourly_5.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(4).getValue()));
                weather_status_hourly_6.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(5).getValue()));
                weather_status_hourly_7.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(6).getValue()));
                weather_status_hourly_8.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(7).getValue()));
                weather_status_hourly_9.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(8).getValue()));
                weather_status_hourly_10.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(9).getValue()));
                weather_status_hourly_11.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(10).getValue()));
                weather_status_hourly_12.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(11).getValue()));
                weather_status_hourly_13.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(12).getValue()));
                weather_status_hourly_14.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(13).getValue()));
                weather_status_hourly_15.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(14).getValue()));
                weather_status_hourly_16.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(15).getValue()));
                weather_status_hourly_17.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(16).getValue()));
                weather_status_hourly_18.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(17).getValue()));
                weather_status_hourly_19.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(18).getValue()));
                weather_status_hourly_20.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(19).getValue()));
                weather_status_hourly_21.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(20).getValue()));
                weather_status_hourly_22.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(21).getValue()));
                weather_status_hourly_23.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(22).getValue()));
                weather_status_hourly_24.setText(GetWeatherTxtUtil.getWeatherTxt(weatherRootBean.getResult().getHourly().getSkycon().get(23).getValue()));

                weather_icon_hourly_1.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(0).getValue()));
                weather_icon_hourly_2.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(1).getValue()));
                weather_icon_hourly_3.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(2).getValue()));
                weather_icon_hourly_4.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(3).getValue()));
                weather_icon_hourly_5.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(4).getValue()));
                weather_icon_hourly_6.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(5).getValue()));
                weather_icon_hourly_7.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(6).getValue()));
                weather_icon_hourly_8.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(7).getValue()));
                weather_icon_hourly_9.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(8).getValue()));
                weather_icon_hourly_10.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(9).getValue()));
                weather_icon_hourly_11.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(10).getValue()));
                weather_icon_hourly_12.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(11).getValue()));
                weather_icon_hourly_13.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(12).getValue()));
                weather_icon_hourly_14.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(13).getValue()));
                weather_icon_hourly_15.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(14).getValue()));
                weather_icon_hourly_16.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(15).getValue()));
                weather_icon_hourly_17.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(16).getValue()));
                weather_icon_hourly_18.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(17).getValue()));
                weather_icon_hourly_19.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(18).getValue()));
                weather_icon_hourly_20.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(19).getValue()));
                weather_icon_hourly_21.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(20).getValue()));
                weather_icon_hourly_22.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(21).getValue()));
                weather_icon_hourly_23.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(22).getValue()));
                weather_icon_hourly_24.setImageResource(GetImageUtil.getImage(mContext, weatherRootBean.getResult().getHourly().getSkycon().get(23).getValue()));


                /*
                 * 曲线图表
                 * */

                ArrayList<String> xValues = new ArrayList<>();

                for (int i = 0; i < 24; i++) {
                    xValues.add(weatherRootBean.getResult().getHourly().getTemperature().get(i).getDatetime().substring(11, 13) + "时");
                }

                ArrayList<Float> values = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    values.add((float) (int) weatherRootBean.getResult().getHourly().getTemperature().get(i).getValue());
                }

                lineChartView.setValues(values);
                lineChartView.setxValues(xValues);
                lineChartView.setChange();

//                int alertCount = weatherRootBean.getResult().getAlert().getContent().size();
//
//                if (alertCount != 0)
//
//                {
//                    weather_alert.setVisibility(View.VISIBLE);
//                    weather_alert.setText(AlertCodeUtil.getAlertType(weatherRootBean.getResult().getAlert().getContent().get(alertCount - 1).getCode()));
//                }
            }
        });
    }

    private void setSwipeRefreshLayoutVisable(final boolean isVisable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isVisable);
            }
        });
    }

    private void showToast(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
