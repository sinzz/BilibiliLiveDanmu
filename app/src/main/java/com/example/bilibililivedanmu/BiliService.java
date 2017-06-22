package com.example.bilibililivedanmu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BiliService extends Service {
    private BilibiliDM mBilibiliDM;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int roomID = intent.getIntExtra("roomID",0);
        final String url = intent.getStringExtra("url");
        mBilibiliDM = new BilibiliDM(roomID,url);
        mBilibiliDM.start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
