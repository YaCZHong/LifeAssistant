package com.czh.life_assistant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.czh.life_assistant.adapter.FmsListAdapter;
import com.czh.life_assistant.entity.FmsEntity;
import com.czh.life_assistant.util.DBUtil;
import com.czh.life_assistant.view.NiceSpinner;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class FmsListActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private SwipeMenuRecyclerView recycleView;
    private FmsListAdapter adapter;
    private ArrayList<FmsEntity> fmsList = new ArrayList<>();

    private String[] arr_fms_type_code = new String[]{"ZTO", "YTO", "YD", "EMS", "JD", "UC", "DBL", "ZJS"};
    private String[] arr_fms_type = new String[]{"中通", "圆通", "韵达", "邮政", "京东", "优速", "德邦", "宅急送"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmslist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recycleView = findViewById(R.id.recycle_view);

        // 3. 创建侧滑菜单
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem editItem = new SwipeMenuItem(FmsListActivity.this)
                        .setBackgroundColor(Color.parseColor("#bdbdbd")) // 背景颜色
                        .setText("编辑") // 文字
                        .setTextColor(Color.WHITE) // 文字颜色
                        .setTextSize(16) // 文字大小
                        .setWidth(dp2px(96)) // 宽
                        .setHeight(MATCH_PARENT); //高（MATCH_PARENT意为Item多高侧滑菜单多高 （推荐使用））
                SwipeMenuItem deleteItem = new SwipeMenuItem(FmsListActivity.this)
                        .setBackgroundColor(Color.parseColor("#ef5350"))
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(16)
                        .setWidth(dp2px(96))
                        .setHeight(MATCH_PARENT);
                swipeRightMenu.addMenuItem(editItem);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        };
        // 4. 创建侧滑菜单的点击事件
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position

                if (menuPosition == 0) {
                    showEditDialog(adapterPosition);
                } else {
                    //Log.d("onItemClick", "id: "+adapter.getmFmsList().get(adapterPosition).getId());
                    DBUtil.deleteFmsInfoInDb(adapter.getmFmsList().get(adapterPosition).getId());
                    adapter.removeData(adapterPosition);
                }

            }
        };

        // 设置菜单创建器
        recycleView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听
        recycleView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        fmsList = DBUtil.getFmsListFromDb();
        adapter = new FmsListAdapter(this, fmsList);
        adapter.setOnItemClickLitener(new FmsListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                FmsEntity fms = adapter.getmFmsList().get(position);
                Intent intent = new Intent(FmsListActivity.this, FmsActivity.class);
                intent.putExtra("fms_type", fms.getFms_type());
                intent.putExtra("fms_code", fms.getFms_code());
                startActivity(intent);
            }
        });
        recycleView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);


        fab = findViewById(R.id.fab_recycler_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
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

    private void showDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_fms, null);

        final EditText et_fms_code = dialogView.findViewById(R.id.et_fms_code);
        final EditText et_fms_mark = dialogView.findViewById(R.id.et_fms_mark);
        final NiceSpinner niceSpinner = dialogView.findViewById(R.id.nice_spinner);

        List<String> dataset = new LinkedList<>(Arrays.asList("中通", "圆通", "韵达", "邮政", "京东", "优速", "德邦", "宅急送"));
        niceSpinner.attachDataSource(dataset);

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(FmsListActivity.this);

        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }

        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.show();

        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        Button btn_ok = dialogView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fms_code = et_fms_code.getText().toString();
                String fms_mark = et_fms_mark.getText().toString();

                if (TextUtils.isEmpty(fms_code) || TextUtils.isEmpty(fms_mark)) {
                    Toast.makeText(FmsListActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                } else {
                    if (DBUtil.saveFmsToDb(getFmsTypeCode(niceSpinner.getText().toString()), fms_code, fms_mark)) {
                        adapter.setmFmsList(DBUtil.getFmsListFromDb());
                        Toast.makeText(FmsListActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FmsListActivity.this, "订单已存在", Toast.LENGTH_SHORT).show();
                    }

                    mBottomSheetDialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    private void showEditDialog(final int position) {

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_fms, null);

        final EditText et_fms_code = dialogView.findViewById(R.id.et_fms_code);
        final EditText et_fms_mark = dialogView.findViewById(R.id.et_fms_mark);
        final NiceSpinner niceSpinner = dialogView.findViewById(R.id.nice_spinner);

        et_fms_code.setText(adapter.getmFmsList().get(position).getFms_code());
        et_fms_mark.setText(adapter.getmFmsList().get(position).getFms_mark());

        List<String> dataset = new LinkedList<>(Arrays.asList("中通", "圆通", "韵达", "邮政", "京东", "优速", "德邦", "宅急送"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setSelectedIndex(dataset.indexOf(getFmsType(adapter.getmFmsList().get(position).getFms_type())));

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(FmsListActivity.this);

        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }

        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.show();

        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        Button btn_ok = dialogView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fms_code = et_fms_code.getText().toString();
                String fms_mark = et_fms_mark.getText().toString();

                if (TextUtils.isEmpty(fms_code) || TextUtils.isEmpty(fms_mark)) {
                    Toast.makeText(FmsListActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                } else {
                    DBUtil.updateFmsToDb(adapter.getmFmsList().get(position).getId(), getFmsTypeCode(niceSpinner.getText().toString()), fms_code, fms_mark);
                    adapter.setmFmsList(DBUtil.getFmsListFromDb());
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    private String getFmsTypeCode(String fms) {
        for (int i = 0; i < arr_fms_type.length; i++) {
            if (fms.equals(arr_fms_type[i])) {
                return arr_fms_type_code[i];
            }
        }
        return null;
    }

    private String getFmsType(String fms_type_code) {
        for (int i = 0; i < arr_fms_type_code.length; i++) {
            if (fms_type_code.equals(arr_fms_type_code[i])) {
                return arr_fms_type[i];
            }
        }
        return null;
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
