package com.czh.life_assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.czh.life_assistant.entity.TodoEntity;
import com.czh.life_assistant.util.DBUtil;
import com.squareup.picasso.Picasso;

public class TodoActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView todo_status;
    private TextView todo_title;
    private TextView todo_content;
    private TextView todo_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageView = findViewById(R.id.todo_bg);
        Picasso.with(this).load(R.drawable.todo_bg).into(imageView);

        todo_status = findViewById(R.id.todo_status);
        todo_title = findViewById(R.id.todo_title);
        todo_content = findViewById(R.id.todo_content);
        todo_date = findViewById(R.id.todo_date);

        Intent intent = getIntent();
        int id = intent.getIntExtra("TODO_ID", -1);
        if (id != -1) {
            TodoEntity todo = DBUtil.getTodoFromDb(id);
            Log.d("TAG", "todo "+todo);
            todo_status.setText((todo.getIsFinish() == 2) ? "已完成" : "未完成");
            todo_title.setText(todo.getTitle());
            todo_content.setText(todo.getContent());
            todo_date.setText(todo.getDate());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
}
