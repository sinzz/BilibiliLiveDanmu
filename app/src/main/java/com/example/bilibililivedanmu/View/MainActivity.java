package com.example.bilibililivedanmu.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.bilibililivedanmu.R;

import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public  static  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Button roomidButton;
    private EditText roomidEdit;
    private String roomId;
    private static final String roomUrl = "http://live.bilibili.com/";
    private static final String danmuUrl = "livecmt-1.bilibili.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roomidButton = (Button)findViewById(R.id.roomid_button);
        roomidEdit =(EditText) findViewById(R.id.roomid_edit);
        roomidButton.setOnClickListener(this);
    }
    private void getRoomid(final String id) {
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d("1","1");
                    OkHttpClient client = new OkHttpClient();
                    if(TextUtils.isEmpty(id)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                roomidEdit.setError("房间号为空");
                            }
                        });
                        return;
                    }
                    else{
                        Request request = new Request.Builder().url(roomUrl+id).build();
                        Response response = client.newCall(request).execute();
                        if(!response.isSuccessful()){
                            //TODO 处理连接失败
                            Log.d("RoomId","error response");
                        }
                        else{
                            String data = response.body().string();
                            int tmpindexstart = data.indexOf("ROOMID =");
                            int tmpindexend = data.indexOf(';',tmpindexstart);
                            roomId = data.substring(tmpindexstart+8,tmpindexend);
                            Log.d("Debug",roomId);
                            StringBuffer sb = new StringBuffer();
                            sb.append("{\"roomid\":");
                            sb.append(roomId);
                            sb.append(",\"uid\":");
                            long uid = (long) (100000000000000.0+200000000000000.0*Math.random());
                            sb.append(uid);
                            sb.append("}");
                            Log.d("Debug",sb.toString());
                            RequestBody requestBody = RequestBody.create(JSON,sb.toString());
                            Request request1 = new Request.Builder().url(danmuUrl).post(requestBody).build();
                            Response response1 = client.newCall(request).execute();
                            showResponse(response1.body().string());
                            Log.d("Debug",response1.body().string());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();*/


    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TextView textView = (TextView)findViewById(R.id.textView);
                //textView.setText(response);
                Log.d("Debug",response);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roomid_button:  String id = roomidEdit.getText().toString(); getRoomid(id);break;
            default:break;
        }
    }
}
