package com.czh.life_assistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.MainActivity;
import com.czh.life_assistant.R;
import com.czh.life_assistant.SelectCityActivity;
import com.czh.life_assistant.adapter.MainAdapter;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.util.PrefsUtil;
import com.czh.life_assistant.util.RequestLocationUtil;
import com.czh.life_assistant.util.RequestWeatherInfo;

import org.json.JSONException;

public class SettingFragment extends Fragment {

    private WeatherFragment weatherFragment;

    private LinearLayout ll_enable_location;
    private Switch switch_location;

    private LinearLayout ll_select_city;
    private TextView select_city_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        ll_enable_location = view.findViewById(R.id.enable_location);
        switch_location = view.findViewById(R.id.switch_location);
        ll_select_city = view.findViewById(R.id.select_city);
        select_city_tv = view.findViewById(R.id.select_city_tv);

        String isLocation = PrefsUtil.getInfoFromPrefs(getActivity(), "isLocation");
        if (isLocation != null) {
            if (isLocation.equals("YES")) {
                switch_location.setChecked(true);
                ll_select_city.setEnabled(false);
                ll_select_city.setAlpha(0.5f);
            } else {
                switch_location.setChecked(false);
                ll_select_city.setEnabled(true);
                ll_select_city.setAlpha(1f);
            }
        } else {
            switch_location.setChecked(false);
            ll_select_city.setEnabled(true);
            ll_select_city.setAlpha(1f);
        }

        ll_enable_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = switch_location.isChecked();
                switch_location.setChecked(!isChecked);
            }
        });

        switch_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_select_city.setEnabled(false);
                    ll_select_city.setAlpha(0.5f);
                    showLocationDialog();
                    PrefsUtil.saveInfoToPrefs(getActivity(), "isLocation", "YES");
                } else {
                    ll_select_city.setEnabled(true);
                    ll_select_city.setAlpha(1f);
                    PrefsUtil.saveInfoToPrefs(getActivity(), "isLocation", "FALSE");
                }
            }
        });

        String selectCity = PrefsUtil.getInfoFromPrefs(getActivity(), "selectCity");
        if (selectCity != null) {
            select_city_tv.setText(selectCity.split("--")[0]);
        }

        ll_select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), SelectCityActivity.class), 200);
            }
        });

        return view;
    }

    public void showLocationDialog() {
        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locManager != null) {
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                AlertDialog.Builder LocationDialog = new AlertDialog.Builder(getActivity());
                LocationDialog.setTitle("GPS未启用");
                LocationDialog.setMessage("可能导致定位失败或定位不准确");
                LocationDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求定位
                        getLocation();

                    }
                }).create();
                LocationDialog.show();
            } else {
                getLocation();

            }
        }
    }

    private void getLocation() {
        //请求定位
        RequestLocationUtil.requestLocation(getActivity(), new RequestLocationUtil.OnRequestLocationListener() {
            @Override
            public void startRequestLocation() {
                showToast("正在定位...");
            }

            @Override
            public void requestLocationSuccess(Context context, String cityName, String lat_lon) {
                select_city_tv.setText(cityName);
                getWeather(cityName + "--" + lat_lon);
            }

            @Override
            public void requestLocationFailed() {
                showToast("定位失败，请检查网络");
                switch_location.setChecked(false);
                ll_select_city.setEnabled(true);
                ll_select_city.setAlpha(1f);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Log.d("TAG", "onActivityResult--fragment");

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 200) {
            if (data == null) {
                return;
            }
            boolean is_select_city = data.getBooleanExtra("SELECT_CITY", false);
            if (is_select_city) {
                String selectCity = PrefsUtil.getInfoFromPrefs(getActivity(), "selectCity");
                if (selectCity != null) {
                    select_city_tv.setText(selectCity.split("--")[0]);
                    getWeather(selectCity);
                }
            }
        }
    }

    private void getWeather(String selectCity) {
        RequestWeatherInfo.RequestWeather(getActivity(), selectCity.split("--")[0], selectCity.split("--")[1], new RequestWeatherInfo.OnRequestWeatherListener() {
            @Override
            public void startRequestWeather() {
                showToast("正在获取天气...");
            }

            @Override
            public void requestWeatherSuccess(Context context, String cityInfo) {
                Fragment fragment = ((MainAdapter) ((MainActivity) getActivity()).getViewPager().getAdapter()).getItem(0);
                String weatherInfo = PrefsUtil.getInfoFromPrefs(getActivity(), cityInfo + "--weatherInfo");
                if (weatherInfo != null) {
                    try {
                        WeatherRootBean weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                        ((WeatherFragment) fragment).showData(weatherRootBean, cityInfo);
                        showToast("获取天气成功");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void requestWeatherFailed() {
                showToast("获取天气失败");
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