package com.example.intelligentlamp.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.R;
import com.example.intelligentlamp.activities.PlayerActivity;
import com.example.intelligentlamp.classes.Music;

import java.util.ArrayList;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Music> mMusicList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Music music = mMusicList.get(position);
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra(PlayerActivity.MUSIC_NAME, music.getTitle());
                intent.putExtra(PlayerActivity.MUSIC_IMAGE_ID, position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PlayerActivity.MUSIC_ITEM, mMusicList);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = mMusicList.get(position);
        holder.musicTitle.setText(music.getTitle());
        holder.musicArtist.setText(music.getArtist());
        holder.musicDuration.setText(formatTime(music.getDuration()));
//        int imageId = R.drawable.butterfly;
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, music.getAlbumId());
        Glide.with(mContext).load(uri).error(R.drawable.music).into(holder.musicImage);
//        if (holder.musicImage.)Glide.with(mContext).load(imageId).into(holder.musicImage);
    }

//    private String getAlbumArt(int albumid) {
//        String strAlbums = "content://media/external/audio/albums";
//        String[] projection = new String[] {android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART };
//        Cursor cur = this.getContentResolver().query(
//                Uri.parse(strAlbums + "/" + Integer.toString(albumid)),
//                projection, null, null, null);
//        String strPath = null;
//        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
//            cur.moveToNext();
//            strPath = cur.getString(0);
//        }
//        cur.close();
//        cur = null;
//        return strPath;
//    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    public static String formatTime(Long time){                     //将歌曲的时间转换为分秒的制度
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if(min.length() < 2)
            min = "0" + min;
        switch (sec.length()){
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0,2);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView musicImage;
        TextView musicTitle;
        TextView musicArtist;
        TextView musicDuration;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            musicImage = (ImageView) view.findViewById(R.id.music_image);
            musicTitle = (TextView) view.findViewById(R.id.music_title);
            musicArtist = (TextView) view.findViewById(R.id.music_artist);
            musicDuration = (TextView) view.findViewById(R.id.music_duration);
        }
    }

    public MusicAdapter(ArrayList<Music> musicList){
        mMusicList = musicList;
    }
}
