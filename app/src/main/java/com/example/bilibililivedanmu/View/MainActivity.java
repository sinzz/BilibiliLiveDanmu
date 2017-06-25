package com.example.bilibililivedanmu.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bilibililivedanmu.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button roomidButton;
    private EditText roomidEdit;
    //private String roomId;
    //private static final String roomUrl = "http://live.bilibili.com/";
    //private static final String danmuUrl = "livecmt-1.bilibili.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roomidButton = (Button)findViewById(R.id.roomid_button);
        roomidEdit =(EditText) findViewById(R.id.roomid_edit);
        roomidButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roomid_button:
                String id = roomidEdit.getText().toString();
                if(TextUtils.isEmpty(id)){
                    roomidEdit.setError("房间号不能为空");
                    break;
                }
                Intent intent = new Intent(MainActivity.this,DanmuView.class);
                intent.putExtra("roomid",Integer.valueOf(id));
                startActivity(intent);
                break;
            default:break;
        }
    }
}
