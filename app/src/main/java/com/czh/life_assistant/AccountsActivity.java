package com.czh.life_assistant;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.czh.life_assistant.adapter.AccountsViewPagerAdapter;
import com.squareup.picasso.Picasso;

public class AccountsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView iv_toolbar_bg;
    private ViewPager viewPager;
    private AccountsViewPagerAdapter accountsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        iv_toolbar_bg = findViewById(R.id.iv_accounts_toolbar_bg);
        Picasso.with(this).load(R.drawable.ic_accounts_toolbar_bg).into(iv_toolbar_bg);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setElevation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPager = findViewById(R.id.viewPager);
        accountsViewPagerAdapter = new AccountsViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(accountsViewPagerAdapter);
        viewPager.setCurrentItem(accountsViewPagerAdapter.getCount()-1);
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
