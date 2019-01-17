package com.example.intelligentlamp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.classes.Model;
import com.example.intelligentlamp.R;
import com.example.intelligentlamp.activities.AmusementActivity;
import com.example.intelligentlamp.activities.BluetoothActivity;
import com.example.intelligentlamp.activities.MusicActivity;
import com.example.intelligentlamp.activities.LearnActivity;
import com.example.intelligentlamp.activities.LiveActivity;
import com.example.intelligentlamp.activities.PlayerActivity;
import com.example.intelligentlamp.activities.DefinedActivity;

import java.util.List;

/**
 * Created by 76377 on 2017/11/4.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private Context mContext;

    private List<Model> mModelList;

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.model_item,
                parent, false);
        final MainAdapter.ViewHolder holder = new MainAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Model model = mModelList.get(position);
                Intent intent = new Intent();
                switch (model.getName()){
                    case "学习模式":
                        intent = new Intent(mContext, LearnActivity.class);
                        break;
                    case "生活模式":
                        intent = new Intent(mContext, LiveActivity.class);
                        break;
                    case "娱乐模式":
                        intent = new Intent(mContext, AmusementActivity.class);
                        break;
                    case "自定义模式":
                        intent = new Intent(mContext, DefinedActivity.class);
                        break;
                    case "歌曲列表":
                        intent = new Intent(mContext, MusicActivity.class);
                        break;
                    default:
                        intent = new Intent(mContext, BluetoothActivity.class);
                        break;
                }
                intent.putExtra(PlayerActivity.MUSIC_NAME, model.getName());
                intent.putExtra(PlayerActivity.MUSIC_IMAGE_ID, model.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        Model music = mModelList.get(position);
        holder.modelName.setText(music.getName());
        Glide.with(mContext).load(music.getImageId()).into(holder.modelImage);
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView modelImage;
        TextView modelName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            modelImage = (ImageView) view.findViewById(R.id.model_image);
            modelName = (TextView) view.findViewById(R.id.model_name);
        }
    }

    public MainAdapter(List<Model> modelList) {
        mModelList = modelList;
    }
}
