package com.czh.life_assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.czh.life_assistant.adapter.LanguageAdapter;

import java.util.Arrays;


public class SelectLanguageActivity extends AppCompatActivity {

    public static final String[] languageName1 = {"自动检测", "中文", "英语", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语"
            , "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语"
            , "瑞典语", "匈牙利语", "繁体中文", "越南语"};

    public static final String[] languageName2 = {"中文", "英语", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语"
            , "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语"
            , "瑞典语", "匈牙利语", "繁体中文", "越南语"};

    private RecyclerView recyclerView;
    private LanguageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        final String TYPE = intent.getStringExtra("TYPE");

        recyclerView = findViewById(R.id.type_recyclerview);
        if (TYPE.equals("SRC")) {
            adapter = new LanguageAdapter(Arrays.asList(languageName1));
        } else {
            adapter = new LanguageAdapter(Arrays.asList(languageName2));
        }

        adapter.setOnItemClickLitener(new LanguageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                if (TYPE.equals("SRC")) {
                    setResultToMain(languageName1[position]);
                } else {
                    setResultToMain(languageName2[position]);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void setResultToMain(String type) {
        Intent data = new Intent();
        data.putExtra("TYPE_RETURN", type);
        setResult(RESULT_OK, data);
        finish();
    }
}
