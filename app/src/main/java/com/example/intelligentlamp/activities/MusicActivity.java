package com.example.intelligentlamp.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.intelligentlamp.classes.Music;
import com.example.intelligentlamp.adapters.MusicAdapter;
import com.example.intelligentlamp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 76377 on 2017/11/4.
 */

public class MusicActivity extends AppCompatActivity {

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";

//    private Music[] musics = {new Music("Look At Me Now", R.drawable.apple),
//            new Music("Butter-Fly", R.drawable.banana), new Music("成都", R.drawable.orange),
//            new Music("Radioactive", R.drawable.watermelon), new Music("Vision", R.drawable.pear),
//            new Music("不醉不回", R.drawable.grape), new Music("Can't Complain", R.drawable.pineapple),
//            new Music("Nevada", R.drawable.strawberry), new Music("PneumaticTokyo", R.drawable.cherry),
//            new Music("NEXT TO YOU", R.drawable.mango)};

//    private Music[] musics = {new Music("Look At Me Now", R.drawable.lookatme),
//            new Music("Butter-Fly", R.drawable.butterfly),
//            new Music("Radioactive", R.drawable.radioactive),
//            new Music("Visions", R.drawable.vision),
//            new Music("Nevada", R.drawable.nevada),
//            new Music("PneumaticTokyo", R.drawable.pneumatictokyo),
//            new Music("Next To You", R.drawable.nexttoyou),
//            new Music("不醉不会", R.drawable.buzuibuhui)
//    };

    private ArrayList<Music> musicList = new ArrayList<>();

    private MusicAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        String musicName = "音乐列表";
//        Intent intent = getIntent();
//        String musicName = intent.getStringExtra(MUSIC_NAME);
//        int musicImageId = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        TextView userContentText = (TextView) findViewById(R.id.player_music);
        Button backUp = (Button) findViewById(R.id.player_back);
        backUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String musicContent = generateMusicContent(musicName);
        userContentText.setText(musicContent);

        initMusics();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MusicAdapter(musicList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.item_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
    }

    private String generateMusicContent(String musicName) {
        StringBuilder musicContent = new StringBuilder();
        musicContent.append(musicName);
        return musicContent.toString();
    }

    public List<Music> getMusicList(){
        return musicList;
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initMusics();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initMusics() {
        musicList.clear();
//        for (int i = 0; i < musics.length; i++) {
////            Random random = new Random();
////            int index = random.nextInt(musics.length);
//            musicList.add(musics[i]);
//        }
        Activity activity = this;
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            Music music = new Music();

            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));  //文件路径

            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM)); //唱片图片

            long albumId = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)); //唱片图片ID

            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            if (isMusic != 0 && duration/(1000 * 60) >= 1&& !url.contains("ape")) {     //只把1分钟以上的音乐添加到集合当中
                music.setId(id);
                music.setAlbum(album);
                music.setAlbumId(albumId);
                music.setArtist(artist);
                music.setDuration(duration);
                music.setTitle(title);
                music.setUrl(url);
                music.setSize(size);
                musicList.add(music);
            }
        }
        cursor.close();
    }
}
