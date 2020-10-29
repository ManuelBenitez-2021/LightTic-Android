package com.laodev.tictic.Chat;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.laodev.tictic.R;
import com.laodev.tictic.SimpleClasses.Variables;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by AQEEL on 4/3/2018.
 */

class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Chat_GetSet> mDataSet;
    String myID;
    private static final int mychat = 1;
    private static final int friendchat = 2;
    private static final int mychatimage=3;
    private static final int otherchatimage=4;
    private static final int mygifimage=5;
    private static final int othergifimage=6;
    private static final int alert_message = 7;

    private static final int my_audio_message = 8;
    private static final int other_audio_message = 9;



    Context context;
    Integer today_day=0;

    private ChatAdapter.OnItemClickListener listener;
    private ChatAdapter.OnLongClickListener long_listener;

    public interface OnItemClickListener {
        void onItemClick(Chat_GetSet item, View view);
    }

    public interface OnLongClickListener {
        void onLongclick (Chat_GetSet item, View view);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     *      Device id
     */

    ChatAdapter(List<Chat_GetSet> dataSet, String id, Context context, ChatAdapter.OnItemClickListener listener, ChatAdapter.OnLongClickListener long_listener) {
        mDataSet = dataSet;
        this.myID=id;
        this.context=context;
        this.listener = listener;
        this.long_listener=long_listener;
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);

    }


    // this is the all types of view that is used in the chat
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View v = null;
        switch (viewtype){
            // we have 4 type of layout in chat activity text chat of my and other and also
            // image layout of my and other
            case mychat:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_my, viewGroup, false);
                Chatviewholder mychatHolder = new Chatviewholder(v);
                return mychatHolder;
            case friendchat:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_other, viewGroup, false);
                Chatviewholder friendchatHolder = new Chatviewholder(v);
                return friendchatHolder;
            case mychatimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_my, viewGroup, false);
                Chatimageviewholder mychatimageHolder = new Chatimageviewholder(v);
                return mychatimageHolder;
            case otherchatimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_other, viewGroup, false);
                Chatimageviewholder otherchatimageHolder = new Chatimageviewholder(v);
                return otherchatimageHolder;

            case my_audio_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_audio_my, viewGroup, false);
                Chataudioviewholder chataudioviewholder = new Chataudioviewholder(v);
                return chataudioviewholder;

            case other_audio_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_audio_other, viewGroup, false);
                Chataudioviewholder other = new Chataudioviewholder(v);
                return other;

           case mygifimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_gif_my, viewGroup, false);
                Chatimageviewholder mychatgigHolder = new Chatimageviewholder(v);
                return mychatgigHolder;
            case othergifimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_gif_other, viewGroup, false);
                Chatimageviewholder otherchatgifHolder = new Chatimageviewholder(v);
                return otherchatgifHolder;
            case alert_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_alert, viewGroup, false);
                Alertviewholder alertviewholder = new Alertviewholder(v);
                return alertviewholder;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat_GetSet chat = mDataSet.get(position);

        if(chat.getType().equals("text")){
            Chatviewholder chatviewholder=(Chatviewholder) holder;
        // check if the message is from sender or receiver
            if(chat.getSender_id().equals(myID)){
                if(chat.getStatus().equals("1"))
                    chatviewholder.message_seen.setText("Seen at "+ChangeDate_to_time(chat.getTime()));
                else
                    chatviewholder.message_seen.setText("Sent");

            }else {
                chatviewholder.message_seen.setText("");
            }
        // make the group of message by date set the gap of 1 min
        // means message send with in 1 min will show as a group
        if (position != 0) {
            Chat_GetSet chat2 = mDataSet.get(position - 1);
            if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                chatviewholder.datetxt.setVisibility(View.GONE);
            } else {
                chatviewholder.datetxt.setVisibility(View.VISIBLE);
                chatviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
            }
            chatviewholder.message.setText(chat.getText());
        }else {
            chatviewholder.datetxt.setVisibility(View.VISIBLE);
            chatviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
            chatviewholder.message.setText(chat.getText());
        }

        chatviewholder.bind(chat,long_listener);

        }

        else if(chat.getType().equals("image")){
            final Chatimageviewholder chatimageholder=(Chatimageviewholder) holder;
            // check if the message is from sender or receiver
            if(chat.getSender_id().equals(myID)){
                if(chat.getStatus().equals("1"))
                    chatimageholder.message_seen.setText("Seen at "+ChangeDate_to_time(chat.getTime()));
                else
                    chatimageholder.message_seen.setText("Sent");

            }else {
                chatimageholder.message_seen.setText("");
            }
            if(chat.getPic_url().equals("none")){
               if(Chat_Activity.uploading_image_id.equals(chat.getChat_id())){
                chatimageholder.p_bar.setVisibility(View.VISIBLE);
                   chatimageholder.message_seen.setText("");
                }else {
                   chatimageholder.p_bar.setVisibility(View.GONE);
                   chatimageholder.not_send_message_icon.setVisibility(View.VISIBLE);
                   chatimageholder.message_seen.setText("Not delivered. ");
               }
            }else {
                chatimageholder.not_send_message_icon.setVisibility(View.GONE);
                chatimageholder.p_bar.setVisibility(View.GONE);
            }

            // make the group of message by date set the gap of 1 min
            // means message send with in 1 min will show as a group
            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                    chatimageholder.datetxt.setVisibility(View.GONE);
                } else {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
                Picasso.with(context).load(chat.getPic_url()).placeholder(R.drawable.image_placeholder).resize(400,400).centerCrop().into(chatimageholder.chatimage);
            }else {
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                Picasso.with(context).load(chat.getPic_url()).placeholder(R.drawable.image_placeholder).resize(400,400).centerCrop().into(chatimageholder.chatimage);


            }

            chatimageholder.bind(mDataSet.get(position),listener,long_listener);
        }



        else if(chat.getType().equals("audio")){
            final Chataudioviewholder chataudioviewholder=(Chataudioviewholder) holder;
            // check if the message is from sender or receiver
            if(chat.getSender_id().equals(myID)){
                if(chat.getStatus().equals("1"))
                    chataudioviewholder.message_seen.setText("Seen at "+ChangeDate_to_time(chat.getTime()));
                else
                    chataudioviewholder.message_seen.setText("Sent");

            }else {
                chataudioviewholder.message_seen.setText("");
            }
            if(chat.getPic_url().equals("none")){
                if(Chat_Activity.uploading_Audio_id.equals(chat.getChat_id())){
                    chataudioviewholder.p_bar.setVisibility(View.VISIBLE);
                    chataudioviewholder.message_seen.setText("");
                }else {
                    chataudioviewholder.p_bar.setVisibility(View.GONE);
                    chataudioviewholder.not_send_message_icon.setVisibility(View.VISIBLE);
                    chataudioviewholder.message_seen.setText("Not delivered. ");
                }
            }else {
                chataudioviewholder.not_send_message_icon.setVisibility(View.GONE);
                chataudioviewholder.p_bar.setVisibility(View.GONE);
            }

            // make the group of message by date set the gap of 1 min
            // means message send with in 1 min will show as a group
            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                    chataudioviewholder.datetxt.setVisibility(View.GONE);
                } else {
                    chataudioviewholder.datetxt.setVisibility(View.VISIBLE);
                    chataudioviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
            }else {
                chataudioviewholder.datetxt.setVisibility(View.VISIBLE);
                chataudioviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));

            }

            if(Chat_Activity.uploading_Audio_id.equals("none")){

            }

            chataudioviewholder.seekBar.setEnabled(false);

            File fullpath = new File(Environment.getExternalStorageDirectory() +"/Binder/"+chat.chat_id+".mp3");
            if(fullpath.exists()) {
                chataudioviewholder.total_time.setText(getfileduration(Uri.parse(fullpath.getAbsolutePath())));

            }else {
                chataudioviewholder.total_time.setText(null);
            }


            chataudioviewholder.bind(mDataSet.get(position),listener,long_listener);

        }





        else if(chat.getType().equals("gif")){
            final Chatimageviewholder chatimageholder=(Chatimageviewholder) holder;
            // check if the message is from sender or receiver
            if(chat.getSender_id().equals(myID)){
                if(chat.getStatus().equals("1"))
                chatimageholder.message_seen.setText("Seen at "+ChangeDate_to_time(chat.getTime()));
                else
                    chatimageholder.message_seen.setText("Sent");

            }else {
                chatimageholder.message_seen.setText("");
            }
            // make the group of message by date set the gap of 1 min
            // means message send with in 1 min will show as a group
            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                    chatimageholder.datetxt.setVisibility(View.GONE);
                } else {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
                Glide.with(context)
                        .load(Variables.gif_firstpart_chat+chat.getPic_url()+Variables.gif_secondpart_chat)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(chatimageholder.chatimage);
            }else {
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                Glide.with(context)
                        .load(Variables.gif_firstpart_chat+chat.getPic_url()+Variables.gif_secondpart_chat)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(chatimageholder.chatimage);

            }

            chatimageholder.bind(mDataSet.get(position),listener,long_listener);
        }


        else if(chat.getType().equals("delete")){
            Alertviewholder alertviewholder=(Alertviewholder) holder;
            alertviewholder.message.setTextColor(context.getResources().getColor(R.color.delete_message_text));
            alertviewholder.message.setBackground(context.getResources().getDrawable(R.drawable.d_round_gray_background_2));

            alertviewholder.message.setText( "This message is deleted by "+chat.getSender_name());

            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(11, 13).equals(chat.getTimestamp().substring(11, 13))) {
                    alertviewholder.datetxt.setVisibility(View.GONE);
                } else {
                    alertviewholder.datetxt.setVisibility(View.VISIBLE);
                    alertviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }

            }else {
                alertviewholder.datetxt.setVisibility(View.VISIBLE);
                alertviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));

            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        // get the type it view ( given message is from sender or receiver)
        if(mDataSet.get(position).getType().equals("text")){
         if (mDataSet.get(position).sender_id.equals(myID)) {
            return mychat;
            }
        return friendchat;
        }
        else if(mDataSet.get(position).getType().equals("image")) {
            if (mDataSet.get(position).sender_id.equals(myID)) {
                return mychatimage;
            }

            return otherchatimage;

        }
        else if(mDataSet.get(position).getType().equals("audio")) {
            if (mDataSet.get(position).sender_id.equals(myID)) {
                return my_audio_message;
            }
            return other_audio_message;
        }

        else if(mDataSet.get(position).getType().equals("gif")) {
            if (mDataSet.get(position).sender_id.equals(myID)) {
                return mygifimage;
            }

            return othergifimage;
        }
        else {
            return alert_message;
        }
    }

    /**
     * Inner Class for a recycler view
     */

    // this is the all the viewholder in which first is for the text
    class Chatviewholder extends RecyclerView.ViewHolder {
        TextView message,datetxt,message_seen;
        View view;
        public Chatviewholder(View itemView) {
            super(itemView);
            view = itemView;
            this.message = view.findViewById(R.id.msgtxt);
            this.datetxt=view.findViewById(R.id.datetxt);
            message_seen=view.findViewById(R.id.message_seen);
        }

        public void bind(final Chat_GetSet item, final ChatAdapter.OnLongClickListener long_listener) {
            message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_listener.onLongclick(item,v);
                    return false;
                }
            });
        }
    }


    // second is for the chat image
    class Chatimageviewholder extends RecyclerView.ViewHolder {
        ImageView chatimage;
        TextView datetxt,message_seen;
        ProgressBar p_bar;
        ImageView not_send_message_icon;
        View view;
        public Chatimageviewholder(View itemView) {
            super(itemView);
            view = itemView;
            this.chatimage = view.findViewById(R.id.chatimage);
            this.datetxt=view.findViewById(R.id.datetxt);
            message_seen=view.findViewById(R.id.message_seen);
            not_send_message_icon=view.findViewById(R.id.not_send_messsage);
            p_bar=view.findViewById(R.id.p_bar);
        }

        public void bind(final Chat_GetSet item, final ChatAdapter.OnItemClickListener listener,final ChatAdapter.OnLongClickListener long_listener) {

            chatimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item,v);
                }
            });

            chatimage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_listener.onLongclick(item,v);
                    return false;
                }
            });
        }

    }

    // third is for the audio message view that show in the chat
    class Chataudioviewholder extends RecyclerView.ViewHolder{
        TextView datetxt,message_seen;
        ProgressBar p_bar;
        ImageView not_send_message_icon;
        ImageView play_btn;
        SeekBar seekBar;
        TextView total_time;
        LinearLayout audio_bubble;


        View view;
        public Chataudioviewholder(View itemView) {
            super(itemView);
            view = itemView;
            audio_bubble=view.findViewById(R.id.audio_bubble);
            datetxt=view.findViewById(R.id.datetxt);
            message_seen=view.findViewById(R.id.message_seen);
            not_send_message_icon=view.findViewById(R.id.not_send_messsage);
            p_bar=view.findViewById(R.id.p_bar);
            this.play_btn=view.findViewById(R.id.play_btn);
            this.seekBar=(SeekBar) view.findViewById(R.id.seek_bar);
            this.total_time=(TextView)view.findViewById(R.id.total_time);

        }

        public void bind(final Chat_GetSet item, final ChatAdapter.OnItemClickListener listener,final ChatAdapter.OnLongClickListener long_listener) {



            audio_bubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item,v);
                }
            });

            audio_bubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_listener.onLongclick(item,v);
                    return false;
                }
            });


        }


    }

    // forth is for the alert type view
    class Alertviewholder extends RecyclerView.ViewHolder {
        TextView message,datetxt;
        View view;
        public Alertviewholder(View itemView) {
            super(itemView);
            view = itemView;
            this.message = view.findViewById(R.id.message);
            this.datetxt = view.findViewById(R.id.datetxt);
        }

    }





    // change the date into (today ,yesterday and date)
    public String ChangeDate(String date){

        try {
            long currenttime = System.currentTimeMillis();

            //database date in millisecond
            long databasedate = 0;
            Date d = null;
            try {
                d = Variables.df.parse(date);
                databasedate = d.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = currenttime - databasedate;
            if (difference < 86400000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if (today_day == chatday)
                    return "Today " + sdf.format(d);
                else if ((today_day - chatday) == 1)
                    return "Yesterday " + sdf.format(d);
            } else if (difference < 172800000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if ((today_day - chatday) == 1)
                    return "Yesterday " + sdf.format(d);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            return sdf.format(d);
        }
        catch (Exception e){

        }
        finally {

            return "";
        }

    }


    // change the date into (today ,yesterday and date)
    public String ChangeDate_to_time(String date){
       try {
           Date d = null;
           try {
               d = Variables.df2.parse(date);

           } catch (ParseException e) {
               e.printStackTrace();
           }


           SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
           return sdf.format(d);
       }catch (Exception e){

       }finally {
           return "";
       }

    }



    // get the audio file duration that is store in our directory
    public String getfileduration(Uri uri) {
        try {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        final int file_duration = Integer.parseInt(durationStr);

        long second = (file_duration / 1000) % 60;
        long minute = (file_duration / (1000 * 60)) % 60;

        return String.format("%02d:%02d", minute, second);
    }
         catch (Exception e){

        }
        return null;
    }


}
