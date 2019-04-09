package com.czh.life_assistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.adapter.FmsInfoAdapter;
import com.czh.life_assistant.entity.forfms.FmsJsonParser;
import com.czh.life_assistant.entity.forfms.FmsRootBean;
import com.czh.life_assistant.entity.forfms.Traces;
import com.czh.life_assistant.util.CheckNetworkUtils;
import com.czh.life_assistant.util.KdniaoTrackQueryAPI;

import org.json.JSONException;

import java.util.List;

public class FmsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TextView tv_empty;
    private FmsInfoAdapter adapter;
    private FmsRootBean fmsRootBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        final String fms_type = intent.getStringExtra("fms_type");
        final String fms_code = intent.getStringExtra("fms_code");

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        recyclerView = findViewById(R.id.recycle_view);
        tv_empty = findViewById(R.id.tv_empty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showProgressDialog();

        getFmsInfo(fms_type, fms_code);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //设置刷新监听器
            @Override
            public void onRefresh() {
                getFmsInfo(fms_type, fms_code);
            }
        });

    }

    private void getFmsInfo(final String fms_type, final String fms_code) {

        if (CheckNetworkUtils.isNetworkConnected(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String fmsInfo = KdniaoTrackQueryAPI.getFmsInfo(fms_type, fms_code);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                closeProgressDialog();
                                swipeRefreshLayout.setRefreshing(false);
                                fmsRootBean = FmsJsonParser.getFmsInfo(fmsInfo);
                                List<Traces> traces = fmsRootBean.getTraces();
                                if (traces.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    tv_empty.setVisibility(View.VISIBLE);

                                    Toast.makeText(FmsActivity.this, "查询不到物流信息", Toast.LENGTH_SHORT).show();

                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    tv_empty.setVisibility(View.GONE);
                                    adapter = new FmsInfoAdapter(traces);
                                    recyclerView.setAdapter(adapter);

                                    Toast.makeText(FmsActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                swipeRefreshLayout.setRefreshing(false);
                                closeProgressDialog();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        } else {
            closeProgressDialog();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
            tv_empty.setVisibility(View.VISIBLE);
        }

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

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(FmsActivity.this);
            progressDialog.setMessage("正在查询...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
