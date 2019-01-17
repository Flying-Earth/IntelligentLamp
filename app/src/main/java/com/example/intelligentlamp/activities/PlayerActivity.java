package com.example.intelligentlamp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentlamp.R;
import com.example.intelligentlamp.classes.Music;

import java.util.ArrayList;


public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener{

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";

    public static final String MUSIC_ITEM = "music_item";

    private SeekBar playerBar;

    private ArrayList<MediaPlayer> media = new ArrayList<MediaPlayer>();

//    MediaPlayer mediaPlayer = new MediaPlayer();

//    private BluetoothChatUtil mBlthChatUtil;

    private int position = 0;

    private Thread thread;

    private boolean isChanging = false;

    private Button playerPlay;

    String musicName;

    TextView userContentText;

//    private Music[] musics = {new Music("Look At Me Now", R.drawable.lookatme),
//            new Music("Butter-Fly", R.drawable.butterfly),
//            new Music("Radioactive", R.drawable.radioactive),
//            new Music("Visions", R.drawable.vision),
//            new Music("Nevada", R.drawable.nevada),
//            new Music("PneumaticTokyo", R.drawable.pneumatictokyo),
//            new Music("Next To You", R.drawable.nexttoyou),
//            new Music("不醉不会", R.drawable.buzuibuhui)
//    };
    //private int i=1;

    private ArrayList<Music> musics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        musicName = intent.getStringExtra(MUSIC_NAME);
        position = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        musics = (ArrayList<Music>)intent.getSerializableExtra(MUSIC_ITEM);

        for (Music music : musics) {
            Log.i("Music",music.getUrl());
        }

        userContentText = (TextView) findViewById(R.id.player_music);
        Button backUp = (Button) findViewById(R.id.player_back);
        backUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String musicContent = generateMusicContent(musicName);
        userContentText.setText(musicContent);

        playerBar = (SeekBar) findViewById(R.id.player_bar);
        playerPlay = (Button) findViewById(R.id.player_play);
        Button playerNext = (Button) findViewById(R.id.player_next);
        Button playerPrevious = (Button) findViewById(R.id.player_previous);

        playerPlay.setOnClickListener(this);
        playerNext.setOnClickListener(this);
        playerPrevious.setOnClickListener(this);
        playerBar.setOnSeekBarChangeListener(this);

        if (ContextCompat.checkSelfPermission(PlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlayerActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            initMediaPlayer();
        }
//        mBlthChatUtil = BluetoothChatUtil.getInstance(this);
        playerBar.setMax(media.get(position).getDuration());
        thread = new Thread(runnable);
        thread.start();
        //mBlthChatUtil.registerHandler(mHandler);
    }

//    private Handler mHandler = new Handler(){
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//                case BluetoothChatUtil.STATE_CONNECTED:
//                    //String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
//                    Toast.makeText(PlayerActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
//                    break;
//                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
//                    Toast.makeText(PlayerActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
//                    break;
//                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
//                    Toast.makeText(PlayerActivity.this, "与设备断开连接", Toast.LENGTH_SHORT).show();
//                    break;
//                case BluetoothChatUtil.MESSAGE_READ:{
//                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
//                    String str = new String(buf,0,buf.length);
//                    if (i == 1)
//                        Toast.makeText(PlayerActivity.this, "读成功" + str, Toast.LENGTH_SHORT).show();
//                    i = 0;
//                    break;
//                }
//                case BluetoothChatUtil.MESSAGE_WRITE:{
//                    byte[] buf = (byte[]) msg.obj;
//                    String str = new String(buf,0,buf.length);
//                    Toast.makeText(PlayerActivity.this, "发送成功" + str, Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                default:
//                    break;
//            }
//        };
//    };

    private String generateMusicContent(String musicName) {
        StringBuilder musicContent = new StringBuilder();
        musicContent.append(musicName);
        return musicContent.toString();
    }

    private void initMediaPlayer() {
        media.clear();
        try {
//            File file[] = {new File(Environment.getExternalStorageDirectory(),
//                    "music.mp3"), new File(Environment.getExternalStorageDirectory(),
//                    "butterfly.mp3"), new File(Environment.getExternalStorageDirectory(),
//                    "music1.mp3")};
            for (int i = 0; i < musics.size(); i++) {
//                mediaPlayer.reset();
//                mediaPlayer.setDataSource(musics.get(i).getUrl());
                media.add(i,MediaPlayer.create(this, Uri.parse(musics.get(i).getUrl())));
//                media.add(mediaPlayer);
                Log.i("PlayerActivity", musics.get(i).getTitle());
                Log.i("PlayerActivity", musics.get(i).getUrl());
            }
//            mediaPlayer.reset();
//            mediaPlayer.setDataSource(musics.get(position).getUrl());
//            media.add(mediaPlayer);

//            media.add(MediaPlayer.create(this, R.raw.lookatmenow));
//            media.add(MediaPlayer.create(this, R.raw.butterfly));
//            media.add(MediaPlayer.create(this, R.raw.radioactive));
//            media.add(MediaPlayer.create(this, R.raw.visions));
//            media.add(MediaPlayer.create(this, R.raw.nevada));
//            media.add(MediaPlayer.create(this, R.raw.pneumatictokyo));
//            media.add(MediaPlayer.create(this, R.raw.nexttoyou));
//            media.add(MediaPlayer.create(this, R.raw.buzuibuhui));
//            playerBar.setMax(media.get(position).getDuration());
            //设置SeekBar最大值为音乐文件持续时间
//            media.get(0).setDataSource(file[0].getPath());
            media.get(position).prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        media.get(position).start();
        media.get(position).setOnCompletionListener(this);
        playerBar.setMax(media.get(position).getDuration());
        thread = new Thread(runnable);
        thread.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while ( media.get(position).isPlaying()) {
                // 将SeekBar位置设置到当前播放位置
                playerBar.setProgress(media.get(position).getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(30);
                    //播放进度
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
  //                  finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        String musicContent = "";
        switch (v.getId()) {
            case R.id.player_play:
                if (!media.get(position).isPlaying()) {
                    playerBar.setMax(media.get(position).getDuration());
                    //设置SeekBar最大值为音乐文件持续时间
                    media.get(position).start();
                    playerPlay.setBackgroundResource(R.drawable.player_pause);
                } else {
                    media.get(position).pause();
                    playerPlay.setBackgroundResource(R.drawable.player_play);

                }
                thread = new Thread(runnable);
                thread.start();
//                Toast.makeText(PlayerActivity.this, Integer.toString(position),Toast.LENGTH_SHORT).show();
//                Toast.makeText(PlayerActivity.this, Long.toString(musics.get(position).getAlbumId()),Toast.LENGTH_SHORT).show();
//                Toast.makeText(PlayerActivity.this, musics.get(position).getUrl(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.player_next:
//                media.get(position).pause();
                media.get(position).reset();
                if (position < media.size() - 1)
                    position++;
                else if (  position + 1  == media.size())
                    position = 0;
//                initMediaPlayer();
//                try {
//                    media.get(position).prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                media.get(position).start();
                musicContent = generateMusicContent(musics.get(position).getTitle());
                userContentText.setText(musicContent);
                playerBar.setMax(media.get(position).getDuration());
                //设置SeekBar最大值为音乐文件持续时间
                playerBar.setProgress(0);
                media.get(position).start();
                media.get(position).setOnCompletionListener(this);
                thread = new Thread(runnable);
                thread.start();
                break;
            case R.id.player_previous:
//                media.get(position).pause();
                media.get(position).reset();
                if (position > 0)
                    position--;
                else if (position == 0)
                    position = media.size() - 1;
//                try {
//                    media.get(position).prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                media.get(position).start();
//                initMediaPlayer();
//                try {
//                    media.get(position).prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                musicContent = generateMusicContent(musics.get(position).getTitle());
                userContentText.setText(musicContent);
                playerBar.setMax(media.get(position).getDuration());
                //设置SeekBar最大值为音乐文件持续时间
                playerBar.setProgress(0);
                media.get(position).start();
                media.get(position).setOnCompletionListener(this);
                thread = new Thread(runnable);
                thread.start();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        thread = new Thread(runnable);
//        thread.start();
//        if (seekBar.getProgress() == media.get(progress).getDuration()-1){
//            media.get(position).reset();
//            if (position < media.size() - 1)
//                position++;
//            else if (  position + 1  == media.size())
//                position = 0;
////                initMediaPlayer();
////                try {
////                    media.get(position).prepare();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                media.get(position).start();
//            String musicContent = "";
//            musicContent = generateMusicContent(musics.get(position).getTitle());
//            userContentText.setText(musicContent);
//            playerBar.setMax(media.get(position).getDuration());
//            //设置SeekBar最大值为音乐文件持续时间
//            playerBar.setProgress(0);
//            media.get(position).start();
//            thread = new Thread(runnable);
//            thread.start();
//        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        isChanging = true;
//        thread = new Thread(runnable);
//        thread.start();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        thread = new Thread(runnable);
//        thread.start();
        media.get(position).seekTo(seekBar.getProgress());
        media.get(position).start();
        //SeekBar确定位置后，跳到指定位置
//        thread = new Thread(runnable);
//        thread.start();
    }

//    //Activity从后台重新回到前台时被调用
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (media.get(position) != null) {
//            if (media.get(position).isPlaying()) {
//                media.get(position).start();
//                thread = new Thread(runnable);
//                thread.start();
//            }
//        }
//    }

    //Activity被覆盖到下面或者锁屏时被调用
    @Override
    protected void onPause() {
        super.onPause();
        if (media.get(position) != null) {
            if (media.get(position).isPlaying()) {
                media.get(position).pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (media.get(position) != null) {
            if (!media.get(position).isPlaying()) {
                media.get(position).start();
                thread = new Thread(runnable);
                thread.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (media.get(position) != null) {
            media.get(position).stop();
            media.get(position).release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        media.get(position).reset();
        if (position < media.size() - 1)
            position++;
        else if (  position + 1  == media.size())
            position = 0;
//                initMediaPlayer();
//                try {
//                    media.get(position).prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                media.get(position).start();
        String musicContent = "";
        musicContent = generateMusicContent(musics.get(position).getTitle());
        userContentText.setText(musicContent);
        playerBar.setMax(media.get(position).getDuration());
        //设置SeekBar最大值为音乐文件持续时间
        playerBar.setProgress(0);
        media.get(position).start();
        media.get(position).setOnCompletionListener(this);
        thread = new Thread(runnable);
        thread.start();
    }
}
