package com.laodev.tictic.Chat.Audio;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.laodev.tictic.R;

import nl.changer.audiowife.AudioWife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Play_Audio_F extends Fragment {

    View view;
    Context context;
    ImageButton play_btn,pause_btn;
    SeekBar seekBar;
    TextView duration_time,total_time;

    AudioWife audioWife;


    ImageButton close_btn;

    public Play_Audio_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_play_audio, container, false);

        context=getContext();

        close_btn=view.findViewById(R.id.close_btn);

        play_btn=(ImageButton) view.findViewById(R.id.play_btn);
        pause_btn=(ImageButton)  view.findViewById(R.id.pause_btn);

        seekBar=(SeekBar) view.findViewById(R.id.seek_bar);

        duration_time=(TextView)view.findViewById(R.id.duration_time);
        total_time=(TextView)view.findViewById(R.id.total_time);

        String filepath=getArguments().getString("path");

        Uri uri= Uri.parse(filepath);


        // this is the third partty library that will show get the audio player view
        // and run the audio file and handle the player view itself

        audioWife= AudioWife.getInstance();
        audioWife.init(context, uri)
                .setPlayView(play_btn)
                .setPauseView(pause_btn)
                .setSeekBar(seekBar)
                .setRuntimeView(duration_time)
                .setTotalTimeView(total_time);

        audioWife.play();


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        audioWife.pause();
        audioWife.release();
    }



}
