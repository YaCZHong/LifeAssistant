package com.czh.life_assistant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.TodoEntity;
import com.czh.life_assistant.util.DBUtil;
import com.czh.life_assistant.util.PrefsUtil;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;
    private Context mContext;
    private List<TodoEntity> mTodoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View todo_view;
        CheckBox todo_checkbox;
        TextView todo_title;

        public ViewHolder(View view) {
            super(view);
            todo_view = view;
            todo_checkbox = view.findViewById(R.id.todo_checkbox);
            todo_title = view.findViewById(R.id.todo_title);
        }
    }

    public TodoListAdapter(Context context, List<TodoEntity> todoList) {
        mContext = context;
        mTodoList = todoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_recyclerview_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取当前Item的position
                    int pos = holder.getLayoutPosition();
                    //回调接口的方法
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TodoEntity todo = mTodoList.get(position);
        holder.todo_checkbox.setChecked(todo.getIsFinish() == 2);
        holder.todo_title.setText(todo.getTitle());
        if (todo.getIsFinish() == 2) {
            holder.todo_title.setPaintFlags(holder.todo_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.todo_title.setPaintFlags(holder.todo_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.todo_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                TodoEntity todo = mTodoList.get(holder.getAdapterPosition());

                String watch_mode = PrefsUtil.getInfoFromPrefs(mContext, "WATCH_MODE");
                if (isChecked) {
                    todo.setIsFinish(2);
                    holder.todo_title.setPaintFlags(holder.todo_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    DBUtil.updateTodoInDb(todo.getId(), todo.getTitle(), todo.getContent(), todo.getDate(), 2);
                    if (watch_mode != null) {
                        if (watch_mode.equals("ONLY_UNFINISH")) {
                            removeData(holder.getAdapterPosition());
                        }
                    }

                } else {
                    todo.setIsFinish(1);
                    holder.todo_title.setPaintFlags(holder.todo_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    DBUtil.updateTodoInDb(todo.getId(), todo.getTitle(), todo.getContent(), todo.getDate(), 1);
                    if (watch_mode != null) {
                        if (watch_mode.equals("ONLY_FINISH")) {
                            removeData(holder.getAdapterPosition());
                        }
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    public void setmTodoList(ArrayList<TodoEntity> list) {
        this.mTodoList = list;
        notifyDataSetChanged();
    }

    public List<TodoEntity> getmTodoList() {
        return mTodoList;
    }

    public void addData(int position, TodoEntity todoEntity) {
        mTodoList.add(position, todoEntity);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mTodoList.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
