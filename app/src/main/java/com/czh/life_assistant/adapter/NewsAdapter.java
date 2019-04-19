package com.czh.life_assistant.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.news.NewEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    private OnItemClickLitener mOnItemClickLitener;
    private ArrayList<NewEntity> news;
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tv_title;
        TextView tv_time;
        TextView tv_author_name;
        ImageView iv_image;


        public ViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            tv_title = view.findViewById(R.id.tv_title);
            tv_time = view.findViewById(R.id.tv_time);
            tv_author_name = view.findViewById(R.id.tv_author_name);
            iv_image = view.findViewById(R.id.iv_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        if (mOnItemClickLitener != null) {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取当前Item的position
                    int pos = holder.getLayoutPosition();
                    //回调接口的方法
                    mOnItemClickLitener.onItemClick(holder.card, pos);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewEntity entity = news.get(position);
        holder.tv_title.setText(entity.getTitle());
        holder.tv_time.setText(entity.getTime());
        holder.tv_author_name.setText(entity.getAuthor_name());
        Picasso.with(context).load(entity.getImage_url()).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setData(ArrayList<NewEntity> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public ArrayList<NewEntity> getData() {
        return news;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
