package com.example.bilibililivedanmu.Bean;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bilibililivedanmu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chazz on 2017/6/22.
 */

public class DmAdapter extends RecyclerView.Adapter<DmAdapter.DmViewHolder>{
    private List<DmStruct> items;
    private static final String giftFront = "收到道具";
    private static Map<String,Integer>getColor = new HashMap<>();
    public class DmViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView message;
        public DmViewHolder(View itemView){
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.danmu_item_name);
            message = (TextView)itemView.findViewById(R.id.danmu_item_message);
        }
    }
    public DmAdapter(List<DmStruct>data){
        items = data!=null?data:new ArrayList<DmStruct>();
        //橙色
        getColor.put("小电视",0xFFFF8000);
        getColor.put("总督",0xFFFF8000);
        //粉色
        getColor.put("节奏风暴",0xFFFF80FF);
        getColor.put("提督",0xFFFF80FF);
        //紫色
        getColor.put("喵娘",0xFFAA00EF);
        getColor.put("舰长",0xFFAA00EF);
        //蓝色
        getColor.put("超级壁咚",0xFFAA00EF);
        getColor.put("233",0xFFAA00EF);
        getColor.put("666",0xFFAA00EF);
        //绿色
        getColor.put("壁咚",0xFF20FF00);
        //灰色
        getColor.put("辣条",0xFF888888);
        getColor.put("亿圆",0xFF888888);
        getColor.put("B坷垃",0xFF888888);
        //红色
        getColor.put("收到道具",0xFFDC143C);
        //基佬紫
        getColor.put("VVIP",0xFF800080);
    }
    @Override
    public DmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DmViewHolder holder = new DmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.danmu_item,parent,false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(DmViewHolder holder, int position) {
        DmStruct tmp = items.get(position);
        if(tmp!=null){
            String name = null;
            String message = null;
            int nameColor = 0;
            int messageColor = 0;
            switch (tmp.getType()){
                case MESSAGE:
                    DmData.DmMessage mes = (DmData.DmMessage) tmp.getObj();
                    name = mes.getUser().getUserName();
                    switch (mes.getUser().getGuard()){
                        case 0:
                            nameColor = (0xFF5ABEF0);
                            messageColor = (0xFF888888);
                            break;
                        case 3:
                            nameColor = getColor.get("舰长");
                            messageColor = getColor.get("VVIP");
                            break;
                        case 2:
                            nameColor = (getColor.get("提督"));
                            messageColor = (getColor.get("VVIP"));
                            break;
                        case 1:
                            nameColor = (getColor.get("总督"));
                            messageColor = (getColor.get("VVIP"));
                            break;
                        default:
                            nameColor = (0xFF5ABEF0);
                            messageColor = (0xFF888888);
                            break;
                    }

                    message = mes.getMessage();
                    break;
                case GIFT:
                    DmData.DmGift gift  = (DmData.DmGift)tmp.getObj();
                    name = giftFront;
                    message = gift.getUserName()+" 赠送了 "+gift.getGiftName()+" * "+gift.getNum();
                    try {
                        nameColor = (getColor.get(giftFront));
                        messageColor = (getColor.get(gift.getGiftName()));
                    }catch(Exception e){
                        e.printStackTrace();
                        DebugLog.d(gift.getGiftName());
                    }
                    break;
                default:
                    return;
            }
            holder.name.setText(name);
            holder.message.setText(message);
            holder.name.setTextColor(nameColor);
            holder.message.setTextColor(messageColor);
        }


    }

    @Override
    public int getItemCount() {
        return (items!=null)?items.size():0;
    }
    public void addItem(int position, DmStruct data){
        items.add(position,data);
        notifyItemInserted(position);
    }
    public void removeItem(int position){
        items.remove(position);
        notifyItemRemoved(position);
    }
    public void removeItems(){
        items.subList(1,1001).clear();
        notifyItemRangeRemoved(1,1000);
    }

}
