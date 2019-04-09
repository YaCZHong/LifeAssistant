package com.czh.life_assistant;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.entity.daily_article.DailyArticleJsonParser;
import com.czh.life_assistant.entity.daily_article.DailyArticleRootBean;
import com.czh.life_assistant.util.RequestDailyArticleInfo;

import org.json.JSONException;

public class DailyArticleActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private TextView title;
    private TextView author;
    private TextView content;
    private TextView wc;
    private String curr;
    private String prev;
    private String next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        scrollView = findViewById(R.id.daily_article_scrollView);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        content = findViewById(R.id.content);
        wc = findViewById(R.id.wc);
        scrollView.setVisibility(View.GONE);

        getArticle(null);

    }

    private void getArticle(String date) {
        RequestDailyArticleInfo.requestDailyArticle(date, new RequestDailyArticleInfo.OnRequestDailyArticleListener() {
            @Override
            public void startRequestDailyArticle() {
                showProgressDialog();
            }

            @Override
            public void requestDailyArticleSuccess(String articleInfo) {
                closeProgressDialog();
                try {
                    //showToast(articleInfo);
                    DailyArticleRootBean dailyArticleRootBean = DailyArticleJsonParser.getDailyArticleInfo(articleInfo);
                    curr = dailyArticleRootBean.getData().getDate().getCurr();
                    prev = dailyArticleRootBean.getData().getDate().getPrev();
                    next = dailyArticleRootBean.getData().getDate().getNext();
                    showArticle(dailyArticleRootBean);
                    showToast("加载成功");
                } catch (JSONException e) {
                    showToast("找不到文章啦，试试随机的吧");
                    e.printStackTrace();
                }
            }

            @Override
            public void requestDailyArticleFailed() {
                showToast("加载失败");
                closeProgressDialog();
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
            case R.id.random:
                getArticle("00000000");
                break;
            case R.id.curr:
                getArticle(null);
                break;
            case R.id.prev:
                getArticle(prev);
                break;
            case R.id.next:
                getArticle(next);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.daily_article_menu, menu);
        return true;
    }


    public void showArticle(final DailyArticleRootBean dailyArticleRootBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                title.setText(dailyArticleRootBean.getData().getTitle());
                author.setText(dailyArticleRootBean.getData().getAuthor());
                content.setText(Html.fromHtml(dailyArticleRootBean.getData().getContent()));
                wc.setText("全文完\t\t共" + dailyArticleRootBean.getData().getWc() + "字");
                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(DailyArticleActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DailyArticleActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
