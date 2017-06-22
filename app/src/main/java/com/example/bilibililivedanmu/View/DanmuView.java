package com.example.bilibililivedanmu.View;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.bilibililivedanmu.Bean.DmData;
import com.example.bilibililivedanmu.R;
import com.example.bilibililivedanmu.Socket.DmSocket;

public class DanmuView extends AppCompatActivity implements DmSocket.OnSocketReceiveCallBack{

    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //TODO 接受消息并显示到View
        }
    };
    private DmSocket mDmSocket = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmu_view);
        mDmSocket = new DmSocket(11,"123");
        mDmSocket.setOnSocketReceiveCallBack(this);
        mDmSocket.start();
    }

    @Override
    public void OnReceiveFromServerMsg(DmData.DmType type, Object msg) {
        if(msg!=null){
            Message message = Message.obtain();
            message.what =type.ordinal();
            message.obj = msg;
        }
    }
}
