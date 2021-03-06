package com.czh.life_assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.czh.life_assistant.AlmanacActivity;
import com.czh.life_assistant.DailyArticleActivity;
import com.czh.life_assistant.FmsListActivity;
import com.czh.life_assistant.NewsActivity;
import com.czh.life_assistant.R;
import com.czh.life_assistant.TodoListActivity;
import com.czh.life_assistant.TranslateActivity;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private CardView card_fms;
    private CardView card_todo;
    private CardView card_daily_article;
    private CardView card_translate;
    private CardView card_news;
    private CardView card_almanac;

    private ImageView iv_fms;
    private ImageView iv_todo;
    private ImageView iv_daily_article;
    private ImageView iv_translate;
    private ImageView iv_news;
    private ImageView iv_almanac;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        card_fms = view.findViewById(R.id.card_fms);
        card_todo = view.findViewById(R.id.card_todo);
        card_translate = view.findViewById(R.id.card_translate);
        card_daily_article = view.findViewById(R.id.card_daily_article);
        card_news = view.findViewById(R.id.card_news);
        card_almanac = view.findViewById(R.id.card_almanac);

        iv_fms = view.findViewById(R.id.iv_fms);
        iv_todo = view.findViewById(R.id.iv_todo);
        iv_translate = view.findViewById(R.id.iv_translate);
        iv_daily_article = view.findViewById(R.id.iv_daily_article);
        iv_news = view.findViewById(R.id.iv_news);
        iv_almanac = view.findViewById(R.id.iv_almanac);

        Picasso.with(getActivity()).load(R.drawable.fms).into(iv_fms);
        Picasso.with(getActivity()).load(R.drawable.todo).into(iv_todo);
        Picasso.with(getActivity()).load(R.drawable.translate).into(iv_translate);
        Picasso.with(getActivity()).load(R.drawable.daily_article).into(iv_daily_article);
        Picasso.with(getActivity()).load(R.drawable.news).into(iv_news);
        Picasso.with(getActivity()).load(R.drawable.alnanac).into(iv_almanac);



        card_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), TodoListActivity.class));
            }
        });

        card_daily_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), DailyArticleActivity.class));
            }
        });

        card_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), TranslateActivity.class));
            }
        });

        card_fms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), FmsListActivity.class));
            }
        });

        card_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), NewsActivity.class));
            }
        });

        card_almanac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AlmanacActivity.class));
            }
        });

        return view;
    }
}
