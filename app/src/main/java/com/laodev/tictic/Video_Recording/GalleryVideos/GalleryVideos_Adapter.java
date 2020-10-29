package com.laodev.tictic.Video_Recording.GalleryVideos;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laodev.tictic.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class GalleryVideos_Adapter extends RecyclerView.Adapter<GalleryVideos_Adapter.CustomViewHolder > {

    public Context context;
    private GalleryVideos_Adapter.OnItemClickListener listener;
    private ArrayList<GalleryVideo_Get_Set> dataList;


      public interface OnItemClickListener {
        void onItemClick(int postion, GalleryVideo_Get_Set item, View view);
    }

    public GalleryVideos_Adapter(Context context, ArrayList<GalleryVideo_Get_Set> dataList, GalleryVideos_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public GalleryVideos_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_galleryvideo_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        GalleryVideos_Adapter.CustomViewHolder viewHolder = new GalleryVideos_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }



    class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb_image;

        TextView view_txt;

        public CustomViewHolder(View view) {
            super(view);

            thumb_image=view.findViewById(R.id.thumb_image);
            view_txt=view.findViewById(R.id.view_txt);

        }

        public void bind(final int position,final GalleryVideo_Get_Set item, final GalleryVideos_Adapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,item,v);
                }
            });

        }

    }




    @Override
    public void onBindViewHolder(final GalleryVideos_Adapter.CustomViewHolder holder, final int i) {
        final GalleryVideo_Get_Set item= dataList.get(i);

        holder.view_txt.setText(item.video_time);

        Glide.with( context )
                .load(Uri.fromFile(new File(item.video_path)) )
                .into(holder.thumb_image);

        holder.bind(i,item,listener);

   }

}