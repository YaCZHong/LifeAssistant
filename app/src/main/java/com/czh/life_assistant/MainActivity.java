package com.czh.life_assistant;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.czh.life_assistant.adapter.MainAdapter;
import com.czh.life_assistant.entity.forweather.WeatherJsonParser;
import com.czh.life_assistant.entity.forweather.WeatherRootBean;
import com.czh.life_assistant.fragment.WeatherFragment;
import com.czh.life_assistant.service.NotificationService;
import com.czh.life_assistant.util.PrefsUtil;

import org.json.JSONException;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private IntentFilter intentFilter;
    private WeatherChangeReceiver weatherChangeReceiver;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_weather:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_home:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_setting:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_weather);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_home);
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.navigation_setting);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.czh.life_assistant.weatherUpdate");
        weatherChangeReceiver = new WeatherChangeReceiver();
        registerReceiver(weatherChangeReceiver, intentFilter);

        initView();
        requestPermission();

        /*
         * 获取到数据库
         * */
        Connector.getDatabase();

        String isNotify = PrefsUtil.getInfoFromPrefs(this, "isNotify");
        if (isNotify != null && isNotify.equals("YES")) {
            startService(new Intent(MainActivity.this, NotificationService.class));
        }
    }

    /*
     * 权限的检查和获取
     * */
    private void requestPermission() {

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.vp);
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(pageChangeListener);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /*
     * 请求权限结果
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] pemissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "缺少必要权限，可能导致某些功能异常！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "发生未知错误！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(weatherChangeReceiver);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    class WeatherChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("MainActivity:","Receice...");

            final String selectCity = PrefsUtil.getInfoFromPrefs(MainActivity.this, "selectCity");
            if (selectCity != null) {
                Fragment fragment = ((MainAdapter) getViewPager().getAdapter()).getItem(0);
                String weatherInfo = PrefsUtil.getInfoFromPrefs(MainActivity.this, selectCity.split("--")[0] + "--weatherInfo");
                if (weatherInfo != null) {
                    try {
                        WeatherRootBean weatherRootBean = WeatherJsonParser.getWeatherInfo(weatherInfo);
                        ((WeatherFragment) fragment).showData(weatherRootBean, selectCity);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
