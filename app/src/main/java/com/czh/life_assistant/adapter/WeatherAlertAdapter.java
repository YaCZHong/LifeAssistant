package com.czh.life_assistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.AlertEntity;

import java.util.ArrayList;

public class WeatherAlertAdapter extends RecyclerView.Adapter<WeatherAlertAdapter.ViewHolder>{

    private ArrayList<AlertEntity> arrayList;
    private Context context;

    public WeatherAlertAdapter(Context context,ArrayList<AlertEntity> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_alert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAlertAdapter.ViewHolder holder, int position) {
        AlertEntity entity = arrayList.get(position);
        holder.alert_title.setText(entity.getAlert_title());
        holder.alert_content.setText(entity.getAlert_content());
        holder.alert_status.setText(entity.getAlert_status());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView alert_title;
        TextView alert_content;
        TextView alert_status;

        public ViewHolder(View itemView) {
            super(itemView);
            alert_title = itemView.findViewById(R.id.alert_title);
            alert_content = itemView.findViewById(R.id.alert_content);
            alert_status = itemView.findViewById(R.id.alert_status);
        }
    }
}
