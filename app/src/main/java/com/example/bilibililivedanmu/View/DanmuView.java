package com.example.bilibililivedanmu.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bilibililivedanmu.Bean.DmAdapter;
import com.example.bilibililivedanmu.Bean.DmData;
import com.example.bilibililivedanmu.Bean.DmStruct;
import com.example.bilibililivedanmu.R;
import com.example.bilibililivedanmu.Socket.DmSocket;

import java.util.ArrayList;
import java.util.List;

public class DanmuView extends AppCompatActivity implements DmSocket.OnSocketReceiveCallBack{
    private RecyclerView mRecyclerView;
    private DmAdapter dmAdapter;
    private FloatingActionButton mFAB;
    private static final int MAX_MESSAGE=5000+100;
    private boolean isLastItem = false;
    private static final String DmService = "livecmt-1.bilibili.com";
    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10086:
                    int ItemCount = dmAdapter.getItemCount();
                    dmAdapter.addItem(ItemCount,(DmStruct) msg.obj);
                    if(!isLastItem)
                        mRecyclerView.smoothScrollToPosition(ItemCount);
                    if(ItemCount>MAX_MESSAGE){
                        dmAdapter.removeItems();
                    }
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
        mFAB = (FloatingActionButton)findViewById(R.id.fab_button);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(dmAdapter.getItemCount());
            }
        });
        Intent intent = this.getIntent();
        int roomid = intent.getIntExtra("roomid",0);
        this.setTitle("Live:"+Integer.toString(roomid));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<DmStruct> list = new ArrayList<>();
        DmStruct tmp = new DmStruct(DmData.DmType.MESSAGE,DmData.getInstance().new DmMessage(1,1,"Welcome",1,"admin",1,1,1,1,1,1));
        list.add(tmp);
        dmAdapter = new DmAdapter(list);
        mRecyclerView.setAdapter(dmAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //向上滑动时
                if(dy<0){
                    isLastItem = true;
                    mFAB.show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //判断状态为不滚动
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if(lastVisibleItem+1==(totalItemCount)){
                        isLastItem = false;
                        mFAB.hide();
                    }
                }
            }
        });
        mDmSocket = new DmSocket(roomid,DmService);
        mDmSocket.setOnSocketReceiveCallBack(this);
        mDmSocket.start();
    }

    @Override
    public void OnReceiveFromServerMsg(DmStruct msg) {
        if(msg!=null&&msg.getType()!= DmData.DmType.OTHER){
            //DebugLog.d("test","OnReceiveFromServerMsg");
            Message message = new Message();
            message.what = 10086;
            message.obj = msg;
            mHandler.sendMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDmSocket.close();
        mHandler.removeCallbacksAndMessages(null);
    }
}
