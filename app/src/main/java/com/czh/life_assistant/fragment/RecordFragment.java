package com.czh.life_assistant.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.adapter.RecordListAdapter;
import com.czh.life_assistant.entity.accounts.RecordEntity;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class RecordFragment extends Fragment {

    private RelativeLayout rl_NoRecords;
    private RelativeLayout rl_Records;
    private String date;
    private TextView tv_date;
    private RecyclerView record_recyclerView;
    private RecordListAdapter adapter;
    private ArrayList<RecordEntity> records = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        initView(view);
        if (records.size() != 0) {
            record_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RecordListAdapter(getActivity(), records);
            adapter.setData(records);
            record_recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @SuppressLint("ValidFragment")
    public RecordFragment(String date) {
        super();
        this.date = date;
        records.add(new RecordEntity());
        records.add(new RecordEntity());
        records.add(new RecordEntity());
        records.add(new RecordEntity());
        records.add(new RecordEntity());
        records.add(new RecordEntity());
        records.add(new RecordEntity());
        records.add(new RecordEntity());
    }

    private void initView(View view) {

        rl_NoRecords = view.findViewById(R.id.rl_NoRecords);
        rl_Records = view.findViewById(R.id.rl_Records);
        tv_date = view.findViewById(R.id.tv_date);
        record_recyclerView = view.findViewById(R.id.record_recyclerView);

        if (records.size() != 0) {
            rl_NoRecords.setVisibility(View.GONE);
        } else {
            rl_Records.setVisibility(View.GONE);
        }
    }
}
