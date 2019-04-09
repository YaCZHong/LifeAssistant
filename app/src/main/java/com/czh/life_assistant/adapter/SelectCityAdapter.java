package com.czh.life_assistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.czh.life_assistant.R;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

public class SelectCityAdapter extends ArrayAdapter {

    private Context context;
    private List<String> list;
    private int ResId;

    public SelectCityAdapter(Context context, int ResId, List<String> list) {
        super(context, ResId, list);
        this.context = context;
        this.list = list;
        this.ResId = ResId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String city = list.get(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(ResId, parent, false);
            holder = new ViewHolder();
            holder.city_name = view.findViewById(R.id.city_name);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.city_name.setText(city);
        return view;
    }

    class ViewHolder {
        TextView city_name;
    }
}
