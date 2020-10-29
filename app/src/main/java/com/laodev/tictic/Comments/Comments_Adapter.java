package com.laodev.tictic.Comments;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laodev.tictic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Comments_Adapter extends RecyclerView.Adapter<Comments_Adapter.CustomViewHolder > {

    public Context context;
    private Comments_Adapter.OnItemClickListener listener;
    private ArrayList<Comment_Get_Set> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Comment_Get_Set item, View view);
    }

    public Comments_Adapter(Context context, ArrayList<Comment_Get_Set> dataList, Comments_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Comments_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Comments_Adapter.CustomViewHolder viewHolder = new Comments_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }


    @Override
    public void onBindViewHolder(final Comments_Adapter.CustomViewHolder holder, final int i) {
        final Comment_Get_Set item= dataList.get(i);


        holder.username.setText(item.first_name+" "+item.last_name);

        try{
        Picasso.with(context).
                load(item.profile_pic)
                .resize(50,50)
                .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                .into(holder.user_pic);

       }catch (Exception e){

       }

        holder.message.setText(item.comments);


        holder.bind(i,item,listener);

   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username,message;
        ImageView user_pic;


        public CustomViewHolder(View view) {
            super(view);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            message=view.findViewById(R.id.message);

        }

        public void bind(final int postion,final Comment_Get_Set item, final Comments_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });

        }


    }





}