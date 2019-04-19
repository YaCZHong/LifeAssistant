package com.czh.life_assistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.forfms.Traces;

import java.util.Collections;
import java.util.List;

public class FmsInfoAdapter extends RecyclerView.Adapter<FmsInfoAdapter.ViewHolder> {

    private List<Traces> mFmsInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fms_AcceptStation;
        TextView tv_fms_AcceptTime;

        public ViewHolder(View view) {
            super(view);
            tv_fms_AcceptStation = view.findViewById(R.id.tv_fms_AcceptStation);
            tv_fms_AcceptTime = view.findViewById(R.id.tv_fms_AcceptTime);
        }
    }

    public FmsInfoAdapter(List<Traces> fmsInfoList) {
        Collections.reverse(fmsInfoList);
        mFmsInfoList = fmsInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fms_detail, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Traces traces = mFmsInfoList.get(position);
        holder.tv_fms_AcceptStation.setText(traces.getAcceptStation());
        holder.tv_fms_AcceptTime.setText(traces.getAcceptTime());
    }

    @Override
    public int getItemCount() {
        return mFmsInfoList.size();
    }

}
