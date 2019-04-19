package com.czh.life_assistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czh.life_assistant.R;
import com.czh.life_assistant.entity.FmsEntity;

import java.util.ArrayList;
import java.util.List;

public class FmsListAdapter extends RecyclerView.Adapter<FmsListAdapter.ViewHolder> {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;
    private Context mContext;
    private List<FmsEntity> mFmsList;
    private String[] arr_fms_type_code = new String[]{"ZTO", "YTO", "YD", "EMS", "JD", "UC", "DBL", "ZJS"};
    private String[] arr_fms_type = new String[]{"中通", "圆通", "韵达", "邮政", "京东", "优速", "德邦", "宅急送"};

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fms_view;
        TextView fms_type;
        TextView fms_mark;
        TextView fms_code;

        public ViewHolder(View view) {
            super(view);
            fms_view = view;
            fms_type = view.findViewById(R.id.fms_type);
            fms_mark = view.findViewById(R.id.fms_mark);
            fms_code = view.findViewById(R.id.fms_code);
        }
    }

    public FmsListAdapter(Context context, List<FmsEntity> fmsList) {
        mContext = context;
        mFmsList = fmsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fms, parent, false);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FmsEntity fms = mFmsList.get(position);
        holder.fms_type.setText(getFmsType(fms.getFms_type()).substring(0, 1));
        holder.fms_mark.setText(fms.getFms_mark());
        holder.fms_code.setText(fms.getFms_code());
    }

    @Override
    public int getItemCount() {
        return mFmsList.size();
    }

    public void setmFmsList(ArrayList<FmsEntity> list) {
        this.mFmsList = list;
        notifyDataSetChanged();
    }

    public List<FmsEntity> getmFmsList() {
        return mFmsList;
    }

    public void addData(int position, FmsEntity fmsEntity) {
        mFmsList.add(position, fmsEntity);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mFmsList.remove(position);
        notifyItemRemoved(position);
    }

    private String getFmsType(String fms_type_code) {
        for (int i = 0; i < arr_fms_type_code.length; i++) {
            if (fms_type_code.equals(arr_fms_type_code[i])) {
                return arr_fms_type[i];
            }
        }
        return null;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
