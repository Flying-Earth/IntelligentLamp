package com.example.intelligentlamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.bluetooth.BluetoothChatUtil;
import com.example.intelligentlamp.R;

public class LearnActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";

    private BluetoothChatUtil mBlthChatUtil;

    private Button light;

    private boolean i = true;

    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String musicName = intent.getStringExtra(MUSIC_NAME);
        int musicImageId = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        TextView userContentText = (TextView) findViewById(R.id.learn_model);
        String musicContent = generateMusicContent(musicName);
        userContentText.setText(musicContent);

        Button back = (Button) findViewById(R.id.learn_back);
        light = (Button) findViewById(R.id.learn_light);
        Button onLed = (Button) findViewById(R.id.learn_on_light);
        Button offLed = (Button) findViewById(R.id.learn_off_light);
        Button learnItem = (Button) findViewById(R.id.learn_item);
        Button learnForward = (Button) findViewById(R.id.learn_forward);
        Button learnBackMusic = (Button) findViewById(R.id.learn_back_music);
        Button learnPlay = (Button) findViewById(R.id.learn_play);
        Button learnPause = (Button) findViewById(R.id.learn_pause);
        Button learnNext = (Button) findViewById(R.id.learn_next);
        Button learnLast = (Button) findViewById(R.id.learn_last);
        //Button learnFirst = (Button) findViewById(R.id.learn_first);
        Button learnCycle = (Button) findViewById(R.id.learn_cycle);
        Button learnRandom = (Button) findViewById(R.id.learn_random);
        Button learnVoiceUp = (Button) findViewById(R.id.learn_voice_up);
        Button learnVoiceDown = (Button) findViewById(R.id.learn_voice_down);

        learnForward.setOnClickListener(this);
        learnBackMusic.setOnClickListener(this);
        learnPlay.setOnClickListener(this);
        learnPause.setOnClickListener(this);
        learnNext.setOnClickListener(this);
        learnLast.setOnClickListener(this);
        //learnFirst.setOnClickListener(this);
        light.setOnClickListener(this);
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

    private String generateMusicContent(String musicName) {
        StringBuilder musicContent = new StringBuilder();
        musicContent.append(musicName);
        return musicContent.toString();
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case BluetoothChatUtil.STATE_CONNECTED:
                    String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
//                    Toast.makeText(LearnActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(LearnActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(LearnActivity.this, "与设备断开连接", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_READ:{
                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    String str = new String(buf,0,buf.length);
                    if (i) { ;
                        i = false;
                    }
                    break;
                }
                case BluetoothChatUtil.MESSAGE_WRITE:{
                    byte[] buf = (byte[]) msg.obj;
                    String str = new String(buf,0,buf.length);
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String messagesend = "";
        i=true;
        switch (v.getId()){
            case R.id.learn_voice_up:
//                Toast.makeText(LearnActivity.this, "增大声音", Toast.LENGTH_SHORT).show();
                 messagesend = "d";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_voice_down:
//                Toast.makeText(LearnActivity.this, "降低声音", Toast.LENGTH_SHORT).show();
                messagesend = "c";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_back:
//                intent = new Intent(LearnActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.learn_back_music:
//                Toast.makeText(LearnActivity.this, "快退", Toast.LENGTH_SHORT).show();
                messagesend = "j";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_cycle:
//                Toast.makeText(LearnActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                messagesend = "h";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_model:
//                Toast.makeText(LearnActivity.this, "第一首", Toast.LENGTH_SHORT).show();
                messagesend = "k";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_forward:
//                Toast.makeText(LearnActivity.this, "快进", Toast.LENGTH_SHORT).show();
                messagesend = "i";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_item:
                intent = new Intent(LearnActivity.this, MusicActivity.class);
                startActivity(intent);
                break;
            case R.id.learn_last:
//                Toast.makeText(LearnActivity.this, "上一首", Toast.LENGTH_SHORT).show();
                messagesend = "f";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_next:
//                Toast.makeText(LearnActivity.this, "下一首", Toast.LENGTH_SHORT).show();
                messagesend = "e";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_off_light:
//                Toast.makeText(LearnActivity.this, "关灯", Toast.LENGTH_SHORT).show();
                messagesend = "m";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_on_light:
//                Toast.makeText(LearnActivity.this, "开灯", Toast.LENGTH_SHORT).show();
                messagesend = "l";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_pause:
//                Toast.makeText(LearnActivity.this, "暂停", Toast.LENGTH_SHORT).show();
                messagesend = "b";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_play:
//                Toast.makeText(LearnActivity.this, "播放", Toast.LENGTH_SHORT).show();
                messagesend = "a";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_random:
//                Toast.makeText(LearnActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                messagesend = "g";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.learn_light:
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
