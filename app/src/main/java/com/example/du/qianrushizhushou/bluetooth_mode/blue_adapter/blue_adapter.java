package com.example.du.qianrushizhushou.bluetooth_mode.blue_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.du.qianrushizhushou.R;

import java.util.List;

public class blue_adapter extends RecyclerView.Adapter<blue_adapter.VH>{
    private List<blue_data_item> mdata;
    public blue_adapter(List<blue_data_item> data){
        this.mdata=data;
    }
    private blue_adapter.OnItemClickListener onItemClickListener;
    @NonNull
    @Override
    public blue_adapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_data_item,parent,false);
        VH vh=new VH(view);
        return vh;
    }


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(blue_adapter.OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final blue_adapter.VH holder, final int position) {
        holder.textView.setText(mdata.get(position).getDevice().getName()+"\n"+mdata.get(position).getDevice().getAddress());
        holder.itemView.getTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener!=null){
                    int pos=holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }




    public class VH extends RecyclerView.ViewHolder {
        TextView textView;

        public VH(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.blue_name);
        }
    }
    public void upData(List<blue_data_item> data){
        this.mdata=data;
        notifyDataSetChanged();
    }
    public void addData(blue_data_item blueDataItem){
        mdata.add(mdata.size(), blueDataItem);
        notifyItemInserted(mdata.size());
    }
}
