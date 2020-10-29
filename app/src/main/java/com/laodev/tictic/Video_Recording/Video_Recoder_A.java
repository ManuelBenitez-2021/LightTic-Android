package com.laodev.tictic.Video_Recording;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coremedia.iso.boxes.Container;
import com.laodev.tictic.R;
import com.laodev.tictic.SegmentProgress.ProgressBarListener;
import com.laodev.tictic.SegmentProgress.SegmentedProgressBar;
import com.laodev.tictic.SimpleClasses.Functions;
import com.laodev.tictic.SimpleClasses.Variables;
import com.laodev.tictic.SoundLists.SoundList_Main_A;
import com.laodev.tictic.Video_Recording.GalleryVideos.GalleryVideos_A;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Video_Recoder_A extends AppCompatActivity implements View.OnClickListener {


    CameraView cameraView;

    int number=0;

    ArrayList<String> videopaths=new ArrayList<>();

    ImageButton record_image;
    ImageButton done_btn;
    boolean is_recording=false;
    boolean is_flash_on=false;

    ImageButton flash_btn;

    SegmentedProgressBar video_progress;

    LinearLayout camera_options;

    ImageButton rotate_camera;

    public static int Sounds_list_Request_code=1;
    TextView add_sound_txt;


    int sec_passed=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hide_navigation();
        setContentView(R.layout.activity_video_recoder);


        Variables.Selected_sound_id="null";
        Variables.recording_duration=Variables.max_recording_duration;



        cameraView = findViewById(R.id.camera);
        camera_options=findViewById(R.id.camera_options);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        record_image=findViewById(R.id.record_image);


        findViewById(R.id.upload_layout).setOnClickListener(this);


        done_btn=findViewById(R.id.done);
        done_btn.setEnabled(false);
        done_btn.setOnClickListener(this);


        rotate_camera=findViewById(R.id.rotate_camera);
        rotate_camera.setOnClickListener(this);
        flash_btn=findViewById(R.id.flash_camera);
        flash_btn.setOnClickListener(this);

        findViewById(R.id.Goback).setOnClickListener(this);

        add_sound_txt=findViewById(R.id.add_sound_txt);
        add_sound_txt.setOnClickListener(this);


        Intent intent=getIntent();
        if(intent.hasExtra("sound_name")){
            add_sound_txt.setText(intent.getStringExtra("sound_name"));
            Variables.Selected_sound_id=intent.getStringExtra("sound_id");
            PreparedAudio();
        }



       // this is code hold to record the video
        final Timer[] timer = {new Timer()};
        record_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    timer[0] =new Timer();

                    timer[0].schedule(new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!is_recording)
                                        Start_or_Stop_Recording();
                                }
                            });

                        }
                    }, 200);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    timer[0].cancel();
                    if(is_recording){
                        Start_or_Stop_Recording();
                    }
                }
                return false;
            }

        });



        initlize_Video_progress();



    }


    public void initlize_Video_progress(){
        sec_passed=0;
        video_progress=findViewById(R.id.video_progress);
        video_progress.enableAutoProgressView(Variables.recording_duration);
        video_progress.setDividerColor(Color.WHITE);
        video_progress.setDividerEnabled(true);
        video_progress.setDividerWidth(4);
        video_progress.setShader(new int[]{Color.CYAN, Color.CYAN, Color.CYAN});

        video_progress.SetListener(new ProgressBarListener() {
            @Override
            public void TimeinMill(long mills) {
                sec_passed = (int) (mills/1000);

                if(sec_passed>(Variables.recording_duration/1000)){
                    Start_or_Stop_Recording();
                }

            }
        });
    }


    // if the Recording is stop then it we start the recording
    // and if the mobile is recording the video then it will stop the recording
    public void Start_or_Stop_Recording(){

        if (!is_recording && sec_passed<(Variables.recording_duration/1000)-1) {
            number=number+1;

            is_recording=true;

            File file = new File(Variables.root + "/" + "myvideo"+(number)+".mp4");
            videopaths.add(Variables.root + "/" + "myvideo"+(number)+".mp4");
            cameraView.captureVideo(file);


            if(audio!=null)
            audio.start();



            video_progress.resume();


            done_btn.setBackgroundResource(R.drawable.ic_not_done);
            done_btn.setEnabled(false);

            record_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_recoding_yes));

            camera_options.setVisibility(View.GONE);
            add_sound_txt.setClickable(false);
            rotate_camera.setVisibility(View.GONE);

        }

        else if (is_recording) {

            is_recording=false;

            video_progress.pause();
            video_progress.addDivider();

            if(audio!=null)
            audio.pause();

            cameraView.stopVideo();


            if(sec_passed>((Variables.recording_duration/1000)/3)) {
                done_btn.setBackgroundResource(R.drawable.ic_done);
                done_btn.setEnabled(true);
            }

            record_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_recoding_no));
            camera_options.setVisibility(View.VISIBLE);

        }

        else if(sec_passed>(Variables.recording_duration/1000)){
            Functions.Show_Alert(this,"Alert","Video only can be a "+(int)Variables.recording_duration/1000+" S");
        }



    }



    // this will apped all the videos parts in one  fullvideo
    private boolean append() {
       final ProgressDialog progressDialog=new ProgressDialog(Video_Recoder_A.this);
        new Thread(new Runnable() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    public void run() {

                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                    }
                });

                ArrayList<String> video_list=new ArrayList<>();
                for (int i=0;i<videopaths.size();i++){

                    File file=new File(videopaths.get(i));
                    if(file.exists()) {

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(Video_Recoder_A.this, Uri.fromFile(file));
                        String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                        boolean isVideo = "yes".equals(hasVideo);

                        if (isVideo && file.length() > 3000) {
                            Log.d("resp", videopaths.get(i));
                            video_list.add(videopaths.get(i));
                        }
                    }
                }



                try {

                    Movie[] inMovies = new Movie[video_list.size()];

                    for (int i=0;i<video_list.size();i++){

                        inMovies[i]= MovieCreator.build(video_list.get(i));
                    }


                    List<Track> videoTracks = new LinkedList<Track>();
                    List<Track> audioTracks = new LinkedList<Track>();
                    for (Movie m : inMovies) {
                        for (Track t : m.getTracks()) {
                            if (t.getHandler().equals("soun")) {
                                audioTracks.add(t);
                            }
                            if (t.getHandler().equals("vide")) {
                                videoTracks.add(t);
                            }
                        }
                    }
                    Movie result = new Movie();
                    if (audioTracks.size() > 0) {
                        result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                    }
                    if (videoTracks.size() > 0) {
                        result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
                    }

                    Container out = new DefaultMp4Builder().build(result);

                    String outputFilePath=null;
                            if(audio!=null){
                                outputFilePath=Variables.outputfile;
                            }else {
                                outputFilePath=Variables.outputfile2;
                            }

                    FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
                    out.writeContainer(fos.getChannel());
                    fos.close();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                            if(audio!=null)
                              Merge_withAudio();
                            else {
                                Go_To_preview_Activity();
                            }

                        }
                    });



                } catch (Exception e) {

                }
            }
        }).start();



        return true;
    }



    // this will add the select audio with the video
    public void Merge_withAudio(){

        String root = Environment.getExternalStorageDirectory().toString();
        String audio_file;
            audio_file =Variables.app_folder+Variables.SelectedAudio_AAC;

        String video = root + "/"+"output.mp4";
        String finaloutput = root + "/"+"output2.mp4";

        Merge_Video_Audio merge_video_audio=new Merge_Video_Audio(Video_Recoder_A.this);
        merge_video_audio.doInBackground(audio_file,video,finaloutput);

    }




    public void RotateCamera(){
        cameraView.toggleFacing();
    }



    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rotate_camera:
                RotateCamera();
                break;

            case R.id.upload_layout:
                Intent upload_intent=new Intent(this, GalleryVideos_A.class);
                startActivity(upload_intent);
                overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
                break;

            case R.id.done:
                append();
                break;

            case R.id.record_image:
                Start_or_Stop_Recording();
                break;

            case R.id.flash_camera:

                if(is_flash_on){
                    is_flash_on=false;
                    cameraView.setFlash(0);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));

                }else {
                    is_flash_on=true;
                    cameraView.setFlash(CameraKit.Constants.FLASH_TORCH);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                }

                break;

            case R.id.Goback:
                onBackPressed();
                break;

            case R.id.add_sound_txt:
                Intent intent =new Intent(this,SoundList_Main_A.class);
                startActivityForResult(intent,Sounds_list_Request_code);
                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                break;

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Sounds_list_Request_code){
            if(data!=null){

             if(data.getStringExtra("isSelected").equals("yes")){
                add_sound_txt.setText(data.getStringExtra("sound_name"));
                 Variables.Selected_sound_id=data.getStringExtra("sound_id");
                 PreparedAudio();
             }

            }

        }
    }



    // this will play the sound with the video when we select the audio
    MediaPlayer audio;
    public  void PreparedAudio(){
        File file=new File(Variables.app_folder+ Variables.SelectedAudio_AAC);
        if(file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(Variables.app_folder + Variables.SelectedAudio_AAC);
                audio.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, Uri.fromFile(file));
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            if(file_duration<Variables.max_recording_duration){
                Variables.recording_duration=file_duration;
                initlize_Video_progress();
            }

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            if (audio != null) {
                audio.stop();
                audio.reset();
                audio.release();
            }
            cameraView.stop();
        
        }catch (Exception e){

        }
    }




    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you Sure? if you Go back you can't undo this action")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        DeleteFile();
                        finish();
                        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);

                    }
                }).show();

    }



    public void Go_To_preview_Activity(){
        Intent intent =new Intent(this,Preview_Video_A.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    // this will delete all the video parts that is create during priviously created video
    int delete_count=0;
    public void DeleteFile(){
        delete_count++;
        File output = new File(Variables.outputfile);
        File output2 = new File(Variables.outputfile2);
        File output_filter_file = new File(Variables.output_filter_file);

        if(output.exists()){
            output.delete();
        }
        if(output2.exists()){

            output2.delete();
        }
        if(output_filter_file.exists()){
            output_filter_file.delete();
        }

        File file = new File(Variables.root + "/" + "myvideo"+(delete_count)+".mp4");
        if(file.exists()){
            file.delete();
            DeleteFile();
        }

    }


    // this will hide the bottom mobile navigation controll
    public void Hide_navigation(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }



}
