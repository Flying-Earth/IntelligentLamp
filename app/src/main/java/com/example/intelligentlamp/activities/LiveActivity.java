package com.example.intelligentlamp.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentlamp.bluetooth.BluetoothChatUtil;
import com.example.intelligentlamp.R;

public class LiveActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";

    private BluetoothChatUtil mBlthChatUtil;

    private Button light;

    private boolean i=true;

    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String musicName = intent.getStringExtra(MUSIC_NAME);
        int musicImageId = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        TextView userContentText = (TextView) findViewById(R.id.live_model);
        String musicContent = generateMusicContent(musicName);
        userContentText.setText(musicContent);

        light = (Button) findViewById(R.id.live_light);
        Button back = (Button) findViewById(R.id.live_back);
        Button onLed = (Button) findViewById(R.id.live_breath);
        Button offLed = (Button) findViewById(R.id.live_off_light);
        Button learnItem = (Button) findViewById(R.id.live_item);
        Button learnForward = (Button) findViewById(R.id.live_forward);
        Button learnBackMusic = (Button) findViewById(R.id.live_back_music);
        Button learnPlay = (Button) findViewById(R.id.live_play);
        Button learnPause = (Button) findViewById(R.id.live_pause);
        Button learnNext = (Button) findViewById(R.id.live_next);
        Button learnLast = (Button) findViewById(R.id.live_last);
        //Button learnFirst = (Button) findViewById(R.id.live_first);
        Button learnCycle = (Button) findViewById(R.id.live_cycle);
        Button learnRandom = (Button) findViewById(R.id.live_random);
        Button learnVoiceUp = (Button) findViewById(R.id.live_voice_up);
        Button learnVoiceDown = (Button) findViewById(R.id.live_voice_down);

        light.setOnClickListener(this);
        learnForward.setOnClickListener(this);
        learnBackMusic.setOnClickListener(this);
        learnPlay.setOnClickListener(this);
        learnPause.setOnClickListener(this);
        learnNext.setOnClickListener(this);
        learnLast.setOnClickListener(this);
        //learnFirst.setOnClickListener(this);
        learnCycle.setOnClickListener(this);
        learnRandom.setOnClickListener(this);
        onLed.setOnClickListener(this);
        offLed.setOnClickListener(this);
        learnItem.setOnClickListener(this);
        back.setOnClickListener(this);
        learnVoiceUp.setOnClickListener(this);
        learnVoiceDown.setOnClickListener(this);

        mBlthChatUtil = BluetoothChatUtil.getInstance(this);
        mBlthChatUtil.registerHandler(mHandler);
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case BluetoothChatUtil.STATE_CONNECTED:
                    String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
//                    Toast.makeText(LiveActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(LiveActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(LiveActivity.this, "与设备断开连接", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_READ:{
                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    String str = new String(buf,0,buf.length);
                    if (i) {
//                        Toast.makeText(LiveActivity.this, "读成功" + str, Toast.LENGTH_SHORT).show();
                        i = false;
                    }
                    break;
                }
                case BluetoothChatUtil.MESSAGE_WRITE:{
                    byte[] buf = (byte[]) msg.obj;
                    String str = new String(buf,0,buf.length);
//                    Toast.makeText(LiveActivity.this, "发送成功" + str, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    private String generateMusicContent(String musicName) {
        StringBuilder musicContent = new StringBuilder();
        musicContent.append(musicName);
        return musicContent.toString();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String messagesend = "";
        i=true;
        switch (v.getId()){
            case R.id.live_voice_up:
//                Toast.makeText(LiveActivity.this, "增大声音", Toast.LENGTH_SHORT).show();
                messagesend = "d";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_voice_down:
//                Toast.makeText(LiveActivity.this, "降低声音", Toast.LENGTH_SHORT).show();
                messagesend = "c";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_back:
//                intent = new Intent(LiveActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.live_back_music:
//                Toast.makeText(LiveActivity.this, "快退", Toast.LENGTH_SHORT).show();
                messagesend = "j";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_cycle:
//                Toast.makeText(LiveActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                messagesend = "h";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_model:
//                Toast.makeText(LiveActivity.this, "第一首", Toast.LENGTH_SHORT).show();
                messagesend = "k";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_forward:
//                Toast.makeText(LiveActivity.this, "快进", Toast.LENGTH_SHORT).show();
                messagesend = "i";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_item:
                intent = new Intent(LiveActivity.this, MusicActivity.class);
                startActivity(intent);
                break;
            case R.id.live_last:
//                Toast.makeText(LiveActivity.this, "上一首", Toast.LENGTH_SHORT).show();
                messagesend = "f";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_next:
//                Toast.makeText(LiveActivity.this, "下一首", Toast.LENGTH_SHORT).show();
                messagesend = "e";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_off_light:
//                Toast.makeText(LiveActivity.this, "关灯", Toast.LENGTH_SHORT).show();
                messagesend = "m";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_breath:
//                Toast.makeText(LiveActivity.this, "呼吸灯", Toast.LENGTH_SHORT).show();
                messagesend = "q";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_pause:
//                Toast.makeText(LiveActivity.this, "暂停", Toast.LENGTH_SHORT).show();
                messagesend = "b";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_play:
//                Toast.makeText(LiveActivity.this, "播放", Toast.LENGTH_SHORT).show();
                messagesend = "a";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_random:
//                Toast.makeText(LiveActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                messagesend = "g";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.live_light:
                if (aBoolean) {
                    messagesend = "Y";
                    aBoolean = false;
                    light.setBackgroundResource(R.drawable.light_setting_on);
                }
                else
                {
                    messagesend = "N";
                    aBoolean = true;
                    light.setBackgroundResource(R.drawable.light_setting);
                }
                mBlthChatUtil.write(messagesend.getBytes());
                break;
        }
    }
}
