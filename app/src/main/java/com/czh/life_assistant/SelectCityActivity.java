package com.czh.life_assistant;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.adapter.SelectCityAdapter;
import com.czh.life_assistant.entity.forcity.Basic;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity implements SelectCityActivityView {

    private SelectCityAdapter adapter;
    private ListView lv;
    private EditText et;

    private List<String> dataList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private static final int TYPE_PROVINCE = 1;
    private static final int TYPE_ADMIN_DISTRICT = 2;
    private static final int TYPE_CITY = 3;
    private int type = TYPE_PROVINCE;
    private SelectCityPresenter presenter;

    private String city;
    private String admin_district;
    private String province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcity);
        presenter = new SelectCityPresenter(this, new SelectCityActivityBiz());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        et = findViewById(R.id.et_city);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.getCityinfo(et.getText().toString());
                }
                return false;
            }
        });

        dataList = presenter.getProvince(getApplicationContext());

        lv = findViewById(R.id.list);
        adapter = new SelectCityAdapter(getApplicationContext(), R.layout.activity_selectcity_listview_item, dataList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("CZH", dataList.get(i));
                if (adapter != null) {
                    adapter = null;
                }
                switch (type) {
                    case 1:
                        province = dataList.get(i);
                        type = TYPE_ADMIN_DISTRICT;
                        dataList = presenter.getAdmin_district(getApplicationContext(), dataList.get(i));
                        break;
                    case 2:
                        admin_district = dataList.get(i);
                        type = TYPE_CITY;
                        dataList = presenter.getCity(getApplicationContext(), dataList.get(i));
                        break;
                    case 3:
                        city = dataList.get(i);
                        String[] info = presenter.getCityInfoFromDb(getApplicationContext(), city, admin_district, province);
                        presenter.saveSelectCity(SelectCityActivity.this, info[0] + "--" + info[2]);

                        setResultToMain(true);
                        break;
                    default:
                        break;
                }
                lv.setAdapter(new SelectCityAdapter(getApplicationContext(), R.layout.activity_selectcity_listview_item, dataList));
            }
        });

        setResultToMain(false);
    }

    public void setResultToMain(Boolean IsSelectCity) {
        Intent data = new Intent();
        data.putExtra("SELECT_CITY", IsSelectCity);
        setResult(RESULT_OK, data);
        if (IsSelectCity) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == TYPE_PROVINCE) {
                return super.onKeyDown(keyCode, event);
            } else if (type == TYPE_ADMIN_DISTRICT) {
                type = TYPE_PROVINCE;
                dataList = presenter.getProvince(getApplicationContext());
                lv.setAdapter(new SelectCityAdapter(getApplicationContext(), R.layout.activity_selectcity_listview_item, dataList));
                return false;
            } else {
                type = TYPE_ADMIN_DISTRICT;
                dataList = presenter.getAdmin_district(getApplicationContext(), province);
                lv.setAdapter(new SelectCityAdapter(getApplicationContext(), R.layout.activity_selectcity_listview_item, dataList));
                return false;
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SelectCityActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showListDialog(final List<Basic> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = list.size();
                final String[] citys = new String[count];
                for (int i = 0; i < count; i++) {
                    citys[i] = list.get(i).getLocation() + "--" + list.get(i).getParent_city() + "--" + list.get(i).getAdmin_area() + "--" + list.get(i).getCnty();
                }
                AlertDialog.Builder listDialog = new AlertDialog.Builder(SelectCityActivity.this);
                listDialog.setTitle("请选择城市");
                listDialog.setItems(citys, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String city_split[] = citys[i].split("--");
                        String lat_lon = list.get(i).getLon() + "," + list.get(i).getLat();
                        presenter.saveSelectCity(SelectCityActivity.this, city_split[0] + "--" + lat_lon);
                        setResultToMain(true);
                    }
                });
                listDialog.show();
            }
        });

    }

    @Override
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (type == TYPE_PROVINCE) {
            super.onBackPressed();
        } else if (type == TYPE_ADMIN_DISTRICT) {
            type = TYPE_PROVINCE;
            dataList = presenter.getProvince(getApplicationContext());
            lv.setAdapter(new SelectCityAdapter(getApplicationContext(), R.layout.activity_selectcity_listview_item, dataList));

        } else {
            type = TYPE_ADMIN_DISTRICT;
            dataList = presenter.getAdmin_district(getApplicationContext(), province);
            lv.setAdapter(new SelectCityAdapter(getApplicationContext(), R.layout.activity_selectcity_listview_item, dataList));
        }
    }
}
