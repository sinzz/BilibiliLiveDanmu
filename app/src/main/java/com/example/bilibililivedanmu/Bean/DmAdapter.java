package com.example.bilibililivedanmu.Bean;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bilibililivedanmu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chazz on 2017/6/22.
 */

public class DmAdapter extends RecyclerView.Adapter<DmAdapter.DmViewHolder>{
    List<DmData.DmMessage> items;
    public class DmViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView message;
        public DmViewHolder(View itemView){
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.danmu_item_name);
            message = (TextView)itemView.findViewById(R.id.danmu_item_message);
        }
    }
    public DmAdapter(List<DmData.DmMessage>data){
        items = data!=null?data:new ArrayList<DmData.DmMessage>();
    }
    @Override
    public DmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DmViewHolder holder = new DmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.danmu_item,parent,false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(DmViewHolder holder, int position) {
        holder.name.setText(items.get(position).getUser().getUserName());
        holder.message.setText(" : "+items.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return (items!=null)?items.size():0;
    }
    public void addItem(int position, DmData.DmMessage data){
        items.add(position,data);
        notifyItemInserted(position);
    }
    public void removeItem(int position){
        items.remove(position);
        notifyItemInserted(position);
    }

}
