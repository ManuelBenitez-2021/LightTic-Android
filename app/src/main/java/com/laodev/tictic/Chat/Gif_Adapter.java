package com.laodev.tictic.Chat;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.laodev.tictic.R;
import com.laodev.tictic.SimpleClasses.Variables;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Gif_Adapter extends RecyclerView.Adapter<Gif_Adapter.CustomViewHolder >{
    public Context context;
    ArrayList<String> gif_list = new ArrayList<>();
    private Gif_Adapter.OnItemClickListener listener;

public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public Gif_Adapter(Context context, ArrayList<String> urllist, Gif_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.gif_list=urllist;
        this.listener = listener;

    }

    @Override
    public Gif_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gif_layout,null);
        Gif_Adapter.CustomViewHolder viewHolder = new Gif_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return gif_list.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView gif_image;

        public CustomViewHolder(View view) {
            super(view);
            gif_image=view.findViewById(R.id.gif_image);
        }

        public void bind(final String item, final Gif_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });


        }

    }


    @Override
    public void onBindViewHolder(final Gif_Adapter.CustomViewHolder holder, final int i) {
        holder.bind(gif_list.get(i),listener);

        Glide.with(context)
                .load(Variables.gif_firstpart+gif_list.get(i)+Variables.gif_secondpart)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(context.getResources().getDrawable(R.drawable.image_placeholder)).centerCrop())
                 .into(holder.gif_image);

        Log.d("resp",Variables.gif_firstpart+gif_list.get(i)+Variables.gif_secondpart);
   }




}