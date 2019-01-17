package com.example.intelligentlamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.intelligentlamp.R;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnClient;
    private Button mBtnServer;
    private Button mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initView();
    }

    private void initView() {
        mBtnClient = (Button)findViewById(R.id.btn_client);
        mBtnServer = (Button)findViewById(R.id.btn_server);
        mBtnBack = (Button)findViewById(R.id.blue_back);

        mBtnClient.setOnClickListener(this);
        mBtnServer.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_client:
                //做客户端，主动连接服务端。
                Intent client = new Intent(this, ClientActivity.class);
                startActivity(client);
                finish();
                break;
            case R.id.btn_server:
                //做服务端，等待客户端的连接请求。
                Intent server = new Intent(this, ServerActivity.class);
                startActivity(server);
                finish();
                break;
            case R.id.blue_back:
//                Intent back = new Intent(this, MainActivity.class);
//                startActivity(back);
                finish();
                break;
            default:
                break;
        }
    }
}
