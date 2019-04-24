package com.czh.life_assistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.adapter.NewsAdapter;
import com.czh.life_assistant.entity.news.NewEntity;
import com.czh.life_assistant.entity.news.NewsJsonParser;
import com.czh.life_assistant.entity.news.NewsRootBean;
import com.czh.life_assistant.util.OkHttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tv_empty;
    private NewsAdapter newsAdapter;
    private ArrayList<NewEntity> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //设置刷新监听器
            @Override
            public void onRefresh() {
                getNews();
            }
        });

        tv_empty = findViewById(R.id.tv_empty);

        recyclerView = findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this);
        newsAdapter.setOnItemClickLitener(new NewsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                NewEntity entity = newsAdapter.getData().get(position);
                Intent intent = new Intent(NewsActivity.this, NewActivity.class);
                intent.putExtra("title", entity.getTitle());
                intent.putExtra("url", entity.getUrl());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(newsAdapter);
        initData();
    }

    private void initData() {
        showProgressDialog();
        getNews();
    }

    private void getNews() {
        OkHttpUtil.sendHttpRequest("http://v.juhe.cn/toutiao/index?type=top&key=86158c3f096d7ad66ad3f8bd4b86e54f", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                closeSwipeRefresh();
                showToast("加载失败");
                showNothing();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String newsInfo = response.body().string();
                closeProgressDialog();
                closeSwipeRefresh();
                try {
                    NewsRootBean newsRootBean = NewsJsonParser.getNews(newsInfo);
                    showView(newsRootBean);
                    showToast("加载成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showNothing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showView(final NewsRootBean newsRootBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                recyclerView.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.GONE);

                if (newsRootBean.getError_code() == 0) {
                    news.clear();
                    for (int i = 0; i < newsRootBean.getResult().getData().size(); i++) {
                        NewEntity entity = new NewEntity();
                        entity.setTitle(newsRootBean.getResult().getData().get(i).getTitle());
                        entity.setTime(newsRootBean.getResult().getData().get(i).getDate());
                        entity.setAuthor_name(newsRootBean.getResult().getData().get(i).getAuthor_name());
                        entity.setUrl(newsRootBean.getResult().getData().get(i).getUrl());
                        entity.setImage_url(newsRootBean.getResult().getData().get(i).getThumbnail_pic_s());
                        news.add(entity);
                    }

                    newsAdapter.setData(news);
                }
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(NewsActivity.this);
            progressDialog.setMessage("正在查询...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void closeSwipeRefresh(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NewsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
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
}
