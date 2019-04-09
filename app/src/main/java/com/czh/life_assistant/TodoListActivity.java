package com.czh.life_assistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czh.life_assistant.adapter.TodoListAdapter;
import com.czh.life_assistant.entity.TodoEntity;
import com.czh.life_assistant.util.DBUtil;
import com.czh.life_assistant.util.PrefsUtil;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class TodoListActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsing_toolbar_layout;
    private Toolbar toolbar;
    private ImageView imageView;
    private SwipeMenuRecyclerView recyclerView;
    private TodoListAdapter adapter;
    private ArrayList<TodoEntity> todoList = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();
    private String today = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        collapsing_toolbar_layout = findViewById(R.id.collapsing_toolbar_layout);
        collapsing_toolbar_layout.setTitle(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change_date:
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TodoListActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
                                collapsing_toolbar_layout.setTitle(date);
                                today = date;
                                adapter.setmTodoList(DBUtil.getTodoListFromDb(date, 0));
                                PrefsUtil.saveInfoToPrefs(TodoListActivity.this, "WATCH_MODE", "ALL");
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        break;
                    case R.id.only_finish:
                        PrefsUtil.saveInfoToPrefs(TodoListActivity.this, "WATCH_MODE", "ONLY_FINISH");
                        adapter.setmTodoList(DBUtil.getTodoListFromDb(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()), 2));
                        break;
                    case R.id.only_unfinish:
                        PrefsUtil.saveInfoToPrefs(TodoListActivity.this, "WATCH_MODE", "ONLY_UNFINISH");
                        adapter.setmTodoList(DBUtil.getTodoListFromDb(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()), 1));
                        break;
                    case R.id.all:
                        PrefsUtil.saveInfoToPrefs(TodoListActivity.this, "WATCH_MODE", "ALL");
                        adapter.setmTodoList(DBUtil.getTodoListFromDb(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()), 0));
                        break;

                }
                return true;
            }
        });

        imageView = findViewById(R.id.iv_todo_toolbar_bg);
        Picasso.with(this).load(R.drawable.todo_toolbar_bg).into(imageView);

        recyclerView = findViewById(R.id.recycle_view);

        // 3. 创建侧滑菜单
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

                SwipeMenuItem editItem = new SwipeMenuItem(TodoListActivity.this)
                        .setBackgroundColor(Color.parseColor("#bdbdbd")) // 背景颜色
                        .setText("编辑") // 文字
                        .setTextColor(Color.WHITE) // 文字颜色
                        .setTextSize(16) // 文字大小
                        .setWidth(dp2px(90)) // 宽
                        .setHeight(MATCH_PARENT); //高（MATCH_PARENT意为Item多高侧滑菜单多高 （推荐使用））
                SwipeMenuItem deleteItem = new SwipeMenuItem(TodoListActivity.this)
                        .setBackgroundColor(Color.parseColor("#ef5350"))
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(16)
                        .setWidth(dp2px(90))
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
                    DBUtil.deleteTodoInDb(adapter.getmTodoList().get(adapterPosition).getId());
                    adapter.removeData(adapterPosition);
                }

            }
        };

        // 设置菜单创建器
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        todoList = DBUtil.getTodoListFromDb(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()), 0);

        adapter = new TodoListAdapter(this, todoList);
        adapter.setOnItemClickLitener(new TodoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TodoEntity todo = adapter.getmTodoList().get(position);
                Intent intent = new Intent(TodoListActivity.this, TodoActivity.class);
                intent.putExtra("TODO_ID", todo.getId());
                startActivity(intent);

            }
        });

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = findViewById(R.id.fab_recycler_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        PrefsUtil.saveInfoToPrefs(TodoListActivity.this, "WATCH_MODE", "ALL");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return true;
    }

    private void showDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_todo, null);

        final EditText et_todo_title = dialogView.findViewById(R.id.et_todo_title);
        final EditText et_todo_content = dialogView.findViewById(R.id.et_todo_content);
        final TextView tv_todo_date = dialogView.findViewById(R.id.tv_todo_date);

        tv_todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TodoListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
                        tv_todo_date.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(TodoListActivity.this);

        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }

        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        Button btn_ok = dialogView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String todo_title = et_todo_title.getText().toString();
                String todo_content = et_todo_content.getText().toString();

                if (TextUtils.isEmpty(todo_title)) {
                    Toast.makeText(TodoListActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                } else {

                    TodoEntity todoEntity = new TodoEntity();
                    todoEntity.setTitle(todo_title);
                    todoEntity.setContent(todo_content);
                    todoEntity.setIsFinish(1);

                    String todo_date = tv_todo_date.getText().toString();

                    if (todo_date.equals("选择日期（默认为标题所示日期）")) {
                        todo_date = today;
                        todoEntity.setDate(today);
                    }

                    if (DBUtil.saveTodoToDb(todo_title, todo_content, todo_date, 1)) {
                        if (todo_date.equals(today)) {
                            switch (PrefsUtil.getInfoFromPrefs(TodoListActivity.this, "WATCH_MODE")) {

                                case "ONLY_UNFINISH":
                                    adapter.setmTodoList(DBUtil.getTodoListFromDb(today, 1));
                                    break;
                                case "ALL":
                                    adapter.setmTodoList(DBUtil.getTodoListFromDb(today, 0));
                                    break;
                            }
                        }
                        Toast.makeText(TodoListActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TodoListActivity.this, "Todo已存在", Toast.LENGTH_SHORT).show();
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

        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.show();
    }

    private void showEditDialog(final int position) {

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_todo, null);

        final EditText et_todo_title = dialogView.findViewById(R.id.et_todo_title);
        final EditText et_todo_content = dialogView.findViewById(R.id.et_todo_content);

        et_todo_title.setText(adapter.getmTodoList().get(position).getTitle());
        et_todo_content.setText(adapter.getmTodoList().get(position).getContent());

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(TodoListActivity.this);

        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }

        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        Button btn_ok = dialogView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String todo_title = et_todo_title.getText().toString();
                String todo_content = et_todo_content.getText().toString();

                if (TextUtils.isEmpty(todo_title)) {
                    Toast.makeText(TodoListActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                } else {

                    TodoEntity todoEntity = new TodoEntity();
                    todoEntity.setTitle(todo_title);
                    todoEntity.setContent(todo_content);
                    todoEntity.setIsFinish(adapter.getmTodoList().get(position).getIsFinish());
                    todoEntity.setDate(today);

                    DBUtil.updateTodoInDb(adapter.getmTodoList().get(position).getId(), todo_title, todo_content, today, adapter.getmTodoList().get(position).getIsFinish());

                    switch (PrefsUtil.getInfoFromPrefs(TodoListActivity.this, "WATCH_MODE")) {
                        case "ONLY_FINISH":
                            adapter.setmTodoList(DBUtil.getTodoListFromDb(today, 2));
                            break;
                        case "ONLY_UNFINISH":
                            adapter.setmTodoList(DBUtil.getTodoListFromDb(today, 1));
                            break;
                        case "ALL":
                            adapter.setmTodoList(DBUtil.getTodoListFromDb(today, 0));
                            break;
                    }
                    Toast.makeText(TodoListActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
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

        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.show();
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
