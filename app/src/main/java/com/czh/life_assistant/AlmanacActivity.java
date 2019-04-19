package com.czh.life_assistant;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.entity.Almanac.AlmanacJsonParser;
import com.czh.life_assistant.entity.Almanac.AlmanacRootBean;
import com.czh.life_assistant.util.OkHttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlmanacActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_yangli;
    private TextView tv_yinli;
    private TextView tv_wuxing;
    private TextView tv_chongsha;
    private TextView tv_baiji;
    private TextView tv_jishen;
    private TextView tv_xiongshen;
    private TextView tv_yi;
    private TextView tv_ji;
    private Date date = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almanac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initView();
        initData();
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        tv_yangli = findViewById(R.id.tv_yangli);
        tv_yinli = findViewById(R.id.tv_yinli);
        tv_wuxing = findViewById(R.id.tv_wuxing);
        tv_chongsha = findViewById(R.id.tv_chongsha);
        tv_baiji = findViewById(R.id.tv_baiji);
        tv_jishen = findViewById(R.id.tv_jishen);
        tv_xiongshen = findViewById(R.id.tv_xiongshen);
        tv_yi = findViewById(R.id.tv_yi);
        tv_ji = findViewById(R.id.tv_ji);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //设置刷新监听器
            @Override
            public void onRefresh() {
                getAlmanacInfo(date);
            }
        });
    }

    private void initData() {
        showProgressDialog();
        getAlmanacInfo(date);
    }

    private void getAlmanacInfo(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        OkHttpUtil.sendHttpRequest("http://v.juhe.cn/laohuangli/d?date=" + formatter.format(date) + "&key=347a8c757e573f957a57103af8353b16", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                showToast("加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String almanacInfo = response.body().string();
                closeProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                try {
                    AlmanacRootBean almanacRootBean = AlmanacJsonParser.getAlmanac(almanacInfo);
                    showView(almanacRootBean);
                    showToast("加载成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showView(final AlmanacRootBean almanacRootBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_yangli.setText(almanacRootBean.getResult().getYangli());
                tv_yinli.setText(almanacRootBean.getResult().getYinli());
                tv_wuxing.setText(almanacRootBean.getResult().getWuxing());
                tv_chongsha.setText(almanacRootBean.getResult().getChongsha());
                tv_baiji.setText(almanacRootBean.getResult().getBaiji());
                tv_jishen.setText(almanacRootBean.getResult().getJishen());
                tv_xiongshen.setText(almanacRootBean.getResult().getXiongshen());
                tv_yi.setText(almanacRootBean.getResult().getYi());
                tv_ji.setText(almanacRootBean.getResult().getJi());
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

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AlmanacActivity.this);
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

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AlmanacActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
