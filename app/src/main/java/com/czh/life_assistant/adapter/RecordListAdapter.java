package com.czh.life_assistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.accounts.RecordEntity;
import com.czh.life_assistant.util.DateUtil;

import java.util.ArrayList;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<RecordEntity> records;

    public RecordListAdapter(Context mContext, ArrayList<RecordEntity> records) {
        this.mContext = mContext;
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.records_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.ViewHolder holder, int position) {
//        holder.imageView_category.setImageResource(records.get(position).getCategory());
//        holder.textView_remark.setText(records.get(position).getRemark());
//        holder.textView_time.setText(DateUtil.getFormattedTime(records.get(position).getTimeStamp()));
//        holder.textView_amount.setText(String.valueOf(records.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setData(ArrayList<RecordEntity> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_category;
        private TextView textView_remark;
        private TextView textView_time;
        private TextView textView_amount;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView_category = itemView.findViewById(R.id.imageView_category);
            textView_remark = itemView.findViewById(R.id.textView_remark);
            textView_time = itemView.findViewById(R.id.textView_time);
            textView_amount = itemView.findViewById(R.id.textView_amount);
        }
    }
}
