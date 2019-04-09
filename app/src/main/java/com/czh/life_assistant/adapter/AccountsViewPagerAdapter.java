package com.czh.life_assistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.czh.life_assistant.fragment.RecordFragment;

import java.util.ArrayList;

public class AccountsViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<RecordFragment> fragments = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

    public AccountsViewPagerAdapter(FragmentManager fm) {
        super(fm);
        init();
    }

    private void init(){

        dates.add("2019-01-01");
        dates.add("2019-01-02");
        dates.add("2019-01-03");

        for(String date:dates){
            fragments.add(new RecordFragment(date));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
