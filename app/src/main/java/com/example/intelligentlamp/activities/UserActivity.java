package com.example.intelligentlamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.R;

public class UserActivity extends AppCompatActivity {

    public static final String MUSIC_NAME = "music_name";

    public static final String MUSIC_IMAGE_ID = "music_image_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        String userName = intent.getStringExtra(MUSIC_NAME);
        byte[] headImage = intent.getByteArrayExtra(MUSIC_IMAGE_ID);
//        int musicImageId = intent.getIntExtra(MUSIC_IMAGE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        ImageView userImageView = (ImageView) findViewById(R.id.user_image_view);
        TextView userContentText = (TextView) findViewById(R.id.music_content_text);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.comment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Comment", Snackbar.LENGTH_SHORT)
                        .setAction("Do", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(UserActivity.this, "You clicked comment",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(userName);
        Glide.with(this).load(headImage).into(userImageView);
        String userContent = generateMusicContent(userName);
        userContentText.setText(userContent);
    }

    private String generateMusicContent(String musicName) {
        StringBuilder musicContent = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            musicContent.append(musicName);
        }
        return musicContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
