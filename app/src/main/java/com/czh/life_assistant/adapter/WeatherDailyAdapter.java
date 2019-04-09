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
import com.czh.life_assistant.entity.DailyEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherDailyAdapter extends RecyclerView.Adapter<WeatherDailyAdapter.ViewHolder> {

    private Context context;
    private List<DailyEntity> list;
    private int ResId;

    public WeatherDailyAdapter(Context context, int ResId, List<DailyEntity> list) {
        this.context = context;
        this.list = list;
        this.ResId = ResId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(ResId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyEntity dailyEntity = list.get(position);
        if (dailyEntity.getImage1_Id() == dailyEntity.getImage2_Id()) {
            Picasso.with(context).load(dailyEntity.getImage1_Id()).into(holder.daily_iv_1);
            holder.tv_interval.setVisibility(View.GONE);
            holder.daily_iv_2.setVisibility(View.GONE);
        } else {
            Picasso.with(context).load(dailyEntity.getImage1_Id()).into(holder.daily_iv_1);
            holder.tv_interval.setVisibility(View.VISIBLE);
            holder.daily_iv_2.setVisibility(View.VISIBLE);
            Picasso.with(context).load(dailyEntity.getImage2_Id()).into(holder.daily_iv_2);
        }

        holder.daily_date.setText(dailyEntity.getDaily_date());
        holder.daily_temp.setText(dailyEntity.getDaily_temp());
        holder.daily_status.setText(dailyEntity.getDaily_status());
        holder.daily_aqi.setText(dailyEntity.getDaily_aqi());
        holder.daily_wind.setText(dailyEntity.getDaily_wind());
        holder.daily_hum.setText(dailyEntity.getDaily_hum());
        holder.daily_uv.setText(dailyEntity.getDaily_uv());
        holder.daily_see.setText(dailyEntity.getDaily_see());
        holder.daily_comfort.setText(dailyEntity.getDaily_comfort());
        holder.daily_sunrise.setText(dailyEntity.getDaily_sunrise());
        holder.daily_sunset.setText(dailyEntity.getDaily_sunset());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView daily_iv_1;
        TextView tv_interval;
        ImageView daily_iv_2;
        TextView daily_date;
        TextView daily_temp;
        TextView daily_status;
        TextView daily_aqi;
        TextView daily_hum;
        TextView daily_wind;
        TextView daily_uv;
        TextView daily_see;
        TextView daily_comfort;
        TextView daily_sunrise;
        TextView daily_sunset;

        public ViewHolder(View view) {
            super(view);
            daily_iv_1 = view.findViewById(R.id.daily_iv_1);
            tv_interval = view.findViewById(R.id.tv_interval);
            daily_iv_2 = view.findViewById(R.id.daily_iv_2);
            daily_date = view.findViewById(R.id.daily_date);
            daily_temp = view.findViewById(R.id.daily_temp);
            daily_status = view.findViewById(R.id.daily_status);
            daily_aqi = view.findViewById(R.id.daily_aqi);
            daily_hum = view.findViewById(R.id.daily_hum);
            daily_wind = view.findViewById(R.id.daily_wind);
            daily_uv = view.findViewById(R.id.daily_uv);
            daily_see = view.findViewById(R.id.daily_see);
            daily_comfort = view.findViewById(R.id.daily_comfort);
            daily_sunrise = view.findViewById(R.id.daily_sunrise);
            daily_sunset = view.findViewById(R.id.daily_sunset);
        }
    }
}
