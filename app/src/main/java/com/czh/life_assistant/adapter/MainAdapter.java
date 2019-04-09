package com.czh.life_assistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.czh.life_assistant.fragment.HomeFragment;
import com.czh.life_assistant.fragment.SettingFragment;
import com.czh.life_assistant.fragment.WeatherFragment;

public class MainAdapter extends FragmentPagerAdapter {

    private WeatherFragment weatherFragment;
    private HomeFragment homeFragment;
    private SettingFragment settingFragment;

    public MainAdapter(FragmentManager fm) {
        super(fm);
        weatherFragment = new WeatherFragment();
        homeFragment = new HomeFragment();
        settingFragment = new SettingFragment();

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = weatherFragment;
                break;
            case 1:
                fragment = homeFragment;
                break;
            case 2:
                fragment = settingFragment;
                break;
        }
        return fragment;
    }
}
