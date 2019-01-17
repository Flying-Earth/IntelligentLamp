package com.example.intelligentlamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentlamp.R;
import com.example.intelligentlamp.bluetooth.BluetoothChatUtil;
import com.example.intelligentlamp.views.SeekBarColorPicker;

public class DefinedActivity extends AppCompatActivity {

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";

    private SeekBarColorPicker mSeekBarColorPicker;
    TextView tvShow;

    private boolean i = true;
    //蓝牙发送数据环节
    private BluetoothChatUtil mBlthChatUtil;

    private String messagesend = "";//蓝牙将要发送的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defined);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String musicName = intent.getStringExtra(MUSIC_NAME);
        int musicImageId = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        TextView userContentText = (TextView) findViewById(R.id.set_model);
        String musicContent = generateMusicContent(musicName);
        userContentText.setText(musicContent);
        mBlthChatUtil = BluetoothChatUtil.getInstance(this);
        mBlthChatUtil.registerHandler(mHandler);

        Button back = (Button) findViewById(R.id.set_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DefinedActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        tvShow = (TextView) findViewById(R.id.tvShow);
        mSeekBarColorPicker = (SeekBarColorPicker) findViewById(R.id.mSeekBarColorPicker);
        mSeekBarColorPicker.setSeekBarColorPickerChangeListener(new SeekBarColorPicker.SeekBarColorPickerChangeListener() {
            @Override
            public void onProgressChange(SeekBarColorPicker seekBarColorPicker, int color, String htmlRgb,int C,int G,int c,int r) {
                Log.e("toHexString","toHexString:"+color);
                findViewById(R.id.mViewResult).setBackgroundColor(color);
                send(r);
                tvShow.setText("圆环" + htmlRgb+"R"+C+"G"+G+"B"+c+"angle"+r);
            }
        });
    }
    public void sendBluetoothMessage (String messagesend){


        byte[] sendByte =HexCommandtoByte(messagesend.getBytes());

        //mBlthChatUtil.write(messagesend.getBytes());

        mBlthChatUtil.write(sendByte);
    }
    public static byte[] HexCommandtoByte(byte[] data) {
        if (data == null) {
            return null;
        }
        int nLength = data.length;
        String strTemString = new String(data, 0, nLength);
        String[] strings = strTemString.split(" ");
        nLength = strings.length;
        data = new byte[nLength];
        for (int i = 0; i < nLength; i++) {
            if (strings[i].length() != 2) {
                data[i] = 00;
                continue;
            }
            try {
                data[i] =(byte)Integer.parseInt(strings[i], 16);
            } catch (Exception e) {
                data[i] = 00;
                continue;
            }
        }
        return data;
    }

    public void send(int angle) {
        i = true;
        if (angle <= 10) {
            messagesend = "c1";
            sendBluetoothMessage(messagesend);
            Toast.makeText(DefinedActivity.this, "10", Toast.LENGTH_LONG).show();
        }
        if (angle <= 20 && angle > 10) {
            messagesend = "c2";
            mBlthChatUtil.write(messagesend.getBytes());
            Toast.makeText(DefinedActivity.this, "20", Toast.LENGTH_LONG).show();
        }
        if (angle <= 30 && angle > 20) {
            messagesend = "c3";
            mBlthChatUtil.write(messagesend.getBytes());
            Toast.makeText(DefinedActivity.this, "30", Toast.LENGTH_LONG).show();
        }
        if (angle <= 40 && angle > 30) {
            messagesend = "c4";
            mBlthChatUtil.write(messagesend.getBytes());
            Toast.makeText(DefinedActivity.this, "40", Toast.LENGTH_LONG).show();
        }
        if (angle <= 50 && angle > 40) {
            messagesend = "c5";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 60 && angle > 50) {
            messagesend = "c6";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 70 && angle > 60) {
            messagesend = "c7";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 80 && angle > 70) {
            messagesend = "c8";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 90 && angle > 80) {
            messagesend = "c9";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 100 && angle > 90) {
            messagesend = "c10";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 100 && angle > 100) {
            messagesend = "c11";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 110 && angle > 100) {
            messagesend = "c12";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 120 && angle > 110) {
            messagesend = "c13";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 130 && angle > 120) {
            messagesend = "c14";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 140 && angle > 130) {
            messagesend = "c15";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 150 && angle > 140) {
            messagesend = "c16";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 160 && angle > 150) {
            messagesend = "c17";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 170 && angle > 160) {
            messagesend = "c18";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 180 && angle > 170) {
            messagesend = "c19";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 190 && angle > 180) {
            messagesend = "c20";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 200 && angle > 190) {
            messagesend = "c21";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 210 && angle > 200) {
            messagesend = "c22";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 220 && angle > 210) {
            messagesend = "c23";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 230 && angle > 220) {
            messagesend = "c24";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 240 && angle > 230) {
            messagesend = "c25";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 250 && angle > 240) {
            messagesend = "c26";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 260 && angle > 250) {
            messagesend = "c27";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if (angle <= 270 && angle > 260){
            messagesend = "c28";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=280&&angle>270) {
            messagesend = "c29";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=290&&angle>280) {
            messagesend = "c30";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=300&&angle>290) {
            messagesend = "c31";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=310&&angle>320) {
            messagesend = "c32";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=330&&angle>320) {
            messagesend = "c33";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=340&&angle>330) {
            messagesend = "c34";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=350&&angle>340) {
            messagesend = "c35";
            mBlthChatUtil.write(messagesend.getBytes());
        }
        if(angle<=360&&angle>350) {
            messagesend = "c36";
            mBlthChatUtil.write(messagesend.getBytes());
        }
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothChatUtil.STATE_CONNECTED:
                    String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
                    Toast.makeText(DefinedActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(DefinedActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(DefinedActivity.this, "与设备断开连接", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_READ: {
                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    String str = new String(buf, 0, buf.length);
                    if (i) {
                        Toast.makeText(DefinedActivity.this, "读成功" + str, Toast.LENGTH_SHORT).show();
                        i = false;
                    }
                    break;
                }
                case BluetoothChatUtil.MESSAGE_WRITE: {
                    byte[] buf = (byte[]) msg.obj;
                    String str = new String(buf, 0, buf.length);
                    Toast.makeText(DefinedActivity.this, "发送成功" + str, Toast.LENGTH_SHORT).show();
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

    public void btBlue(View view) {
        //#01DDFF
        mSeekBarColorPicker.setColorByhtmlRGB("#01DDFF");
    }

    public void btGreen(View view) {
        //#98CB00
        mSeekBarColorPicker.setColorByhtmlRGB("#98CB00");
    }

    public void btRed(View view) {
        //#FF4444
        mSeekBarColorPicker.setColorByhtmlRGB("#ff4444");

    }

}