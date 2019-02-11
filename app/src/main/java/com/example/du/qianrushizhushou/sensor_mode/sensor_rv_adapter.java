package com.example.du.qianrushizhushou.sensor_mode;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.du.*;
import com.example.du.qianrushizhushou.R;
import java.util.List;

public class sensor_rv_adapter extends RecyclerView.Adapter<sensor_rv_adapter.VH> implements View.OnClickListener {

    private View view;
    private List<Sensor_jihe> mdata;

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer)v.getTag());
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setmItemClickListener(OnItemClickListener itemClickListener){
        mItemClickListener=itemClickListener;
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView textView;
        public VH(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.sensor_item);
        }
    }

    public void sensor_rv_adapter(List<Sensor_jihe> data){
        this.mdata=data;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_item,parent,false);
       VH vh=new VH(view);
       view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.textView.setText(mdata.get(position).getSensor_name());
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


}
