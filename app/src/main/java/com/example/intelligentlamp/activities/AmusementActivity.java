package com.example.intelligentlamp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentlamp.R;
import com.example.intelligentlamp.bluetooth.BluetoothChatUtil;

public class AmusementActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";

    private BluetoothChatUtil mBlthChatUtil;

    private boolean i = true;

    private Button light;

    private boolean aBoolean = true;

    private String[] items = new String[] { "呼吸灯", "彩虹灯","彩灯" };
    private String messagesend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amusement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String musicName = intent.getStringExtra(MUSIC_NAME);
        int musicImageId = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        TextView userContentText = (TextView) findViewById(R.id.amuse_model);
        String musicContent = generateMusicContent(musicName);
        userContentText.setText(musicContent);

        light = (Button) findViewById(R.id.amuse_light);
        Button back = (Button) findViewById(R.id.amuse_back);
        Button offLed = (Button) findViewById(R.id.amuse_off_light);
        Button learnItem = (Button) findViewById(R.id.amuse_item);
        Button learnForward = (Button) findViewById(R.id.amuse_forward);
        Button learnBackMusic = (Button) findViewById(R.id.amuse_back_music);
        Button learnPlay = (Button) findViewById(R.id.amuse_play);
        Button learnPause = (Button) findViewById(R.id.amuse_pause);
        Button learnNext = (Button) findViewById(R.id.amuse_next);
        Button learnLast = (Button) findViewById(R.id.amuse_last);
        Button learnCycle = (Button) findViewById(R.id.amuse_cycle);
        Button learnRandom = (Button) findViewById(R.id.amuse_random);
        Button learnColor = (Button) findViewById(R.id.amuse_color);
        Button learnVoiceUp = (Button) findViewById(R.id.amuse_voice_up);
        Button learnVoiceDown = (Button) findViewById(R.id.amuse_voice_down);

        light.setOnClickListener(this);
        learnForward.setOnClickListener(this);
        learnBackMusic.setOnClickListener(this);
        learnPlay.setOnClickListener(this);
        learnPause.setOnClickListener(this);
        learnNext.setOnClickListener(this);
        learnLast.setOnClickListener(this);
        learnCycle.setOnClickListener(this);
        learnRandom.setOnClickListener(this);
        offLed.setOnClickListener(this);
        learnItem.setOnClickListener(this);
        back.setOnClickListener(this);
        learnColor.setOnClickListener(this);
        learnVoiceUp.setOnClickListener(this);
        learnVoiceDown.setOnClickListener(this);

        mBlthChatUtil = BluetoothChatUtil.getInstance(this);
        mBlthChatUtil.registerHandler(mHandler);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothChatUtil.STATE_CONNECTED:
                    String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
//                    Toast.makeText(AmusementActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(AmusementActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(AmusementActivity.this, "与设备断开连接", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_READ: {
                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    String str = new String(buf, 0, buf.length);
                    if (i) {
                        i = false;
                    }
                    break;
                }
                case BluetoothChatUtil.MESSAGE_WRITE: {
                    byte[] buf = (byte[]) msg.obj;
                    String str = new String(buf, 0, buf.length);
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private String generateMusicContent(String musicName) {
        StringBuilder musicContent = new StringBuilder();
        musicContent.append(musicName);
        return musicContent.toString();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        //String messagesend = "";
        i = true;
        switch (v.getId()) {
            case R.id.amuse_voice_up:
                messagesend = "d";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_voice_down:
                messagesend = "c";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_color:
                showDialog();
                break;
            case R.id.amuse_back:
                finish();
                break;
            case R.id.amuse_back_music:
                messagesend = "j";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_cycle:
                messagesend = "h";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_model:
                messagesend = "k";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_forward:
                messagesend = "i";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_item:
                intent = new Intent(AmusementActivity.this, MusicActivity.class);
                startActivity(intent);
                break;
            case R.id.amuse_last:
                messagesend = "f";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_next:
                messagesend = "e";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_off_light:
                messagesend = "m";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_pause:
                messagesend = "b";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_play:
                messagesend = "a";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_random:
                messagesend = "g";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.amuse_light:
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

    private void showDialog() {

        i = true;
        new AlertDialog.Builder(this).setTitle("选择灯光").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        messagesend = "q";
                        mBlthChatUtil.write(messagesend.getBytes());
                        break;
                    case 1:
                        messagesend = "p";
                        mBlthChatUtil.write(messagesend.getBytes());
                        break;
                    case 2:
                        messagesend = "n";
                        mBlthChatUtil.write(messagesend.getBytes());

                        break;
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}