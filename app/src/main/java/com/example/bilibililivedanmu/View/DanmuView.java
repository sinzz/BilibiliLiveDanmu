package com.example.bilibililivedanmu.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bilibililivedanmu.Bean.DebugLog;
import com.example.bilibililivedanmu.Bean.DmAdapter;
import com.example.bilibililivedanmu.Bean.DmData;
import com.example.bilibililivedanmu.R;
import com.example.bilibililivedanmu.Socket.DmSocket;

import java.util.ArrayList;
import java.util.List;

public class DanmuView extends AppCompatActivity implements DmSocket.OnSocketReceiveCallBack{
    private static RecyclerView mRecyclerView;
    public static DmAdapter dmAdapter;
    private static final String DmService = "livecmt-1.bilibili.com";
    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //TODO 接受消息并显示到View
            switch (msg.what){
                case 10086:
                    dmAdapter.addItem(dmAdapter.getItemCount(),(DmData.DmMessage) msg.obj);
                    mRecyclerView.smoothScrollToPosition(dmAdapter.getItemCount());
                    break;

            }
        }
    };
    private DmSocket mDmSocket = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmu_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        Intent intent = this.getIntent();
        int roomid = intent.getIntExtra("roomid",0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<DmData.DmMessage> list = new ArrayList<>();
        DmData.DmMessage tmp = DmData.getInstance().new DmMessage(1,1,"test",1,"test",1,1,1,1,1);
        list.add(tmp);
        dmAdapter = new DmAdapter(list);
        mRecyclerView.setAdapter(dmAdapter);
        mDmSocket = new DmSocket(roomid,DmService);
        mDmSocket.setOnSocketReceiveCallBack(this);
        mDmSocket.start();
    }

    @Override
    public void OnReceiveFromServerMsg(DmData.DmType type, Object msg) {
        if(msg!=null){
            DebugLog.d("test","OnReceiveFromServerMsg");
            Message message = new Message();
            message.what = 10086;
            message.obj = msg;
            mHandler.sendMessage(message);
        }
    }
}
