package com.laodev.tictic.SimpleClasses;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.laodev.tictic.Comments.Comment_Get_Set;
import com.laodev.tictic.R;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.googlecode.mp4parser.authoring.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by AQEEL on 2/20/2019.
 */

public class Functions {



    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static void Show_Alert(Context context,String title,String Message){
       new  AlertDialog.Builder(context)
               .setTitle(title)
               .setMessage(Message)
               .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               }).show();
    }


    public static Dialog dialog;
    public static void Show_loader(Context context,boolean outside_touch, boolean cancleable) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_loading_view);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_white_background));


        CamomileSpinner loader=dialog.findViewById(R.id.loader);
        loader.start();


        if(!outside_touch)
            dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            dialog.setCancelable(false);

        dialog.show();
    }

    public static void cancel_loader(){
        if(dialog!=null){
            dialog.cancel();
        }
    }




    public static float dpToPx(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    public static void Share_through_app(final Activity activity,final String link){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link);
                activity.startActivity(Intent.createChooser(intent, ""));

            }
        }).start();
    }


    public static Bitmap Uri_to_bitmap(Activity activity,Uri uri){
        InputStream imageStream = null;
        try {
            imageStream =activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path=uri.getPath();
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }



    public static String Bitmap_to_base64(Activity activity,Bitmap imagebitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos .toByteArray();
        String base64= Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }


    public static String Uri_to_base64(Activity activity, Uri uri){
        InputStream imageStream = null;
        try {
            imageStream =activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path=uri.getPath();
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos .toByteArray();
        String base64= Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }



    public static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;

        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }







    // Bottom is all the Apis which is mostly used in app we have add it
    // just one time and whenever we need it we will call it

    public static void Call_Api_For_like_video(final Activity activity,
                                               String video_id,String action,
                                               final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("video_id",video_id);
            parameters.put("action",action);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(activity, Variables.likeDislikeVideo, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                api_callBack.OnSuccess(resp);
            }
        });



    }


    public static void Call_Api_For_Send_Comment(final Activity activity, String video_id,String comment ,final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("video_id",video_id);
            parameters.put("comment",comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.postComment, parameters, new Callback() {
            @Override
            public void Responce(String resp) {

                ArrayList<Comment_Get_Set> arrayList=new ArrayList<>();
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    if(code.equals("200")){
                        JSONArray msgArray=response.getJSONArray("msg");
                        for (int i=0;i<msgArray.length();i++) {
                            JSONObject itemdata = msgArray.optJSONObject(i);
                            Comment_Get_Set item=new Comment_Get_Set();
                            item.fb_id=itemdata.optString("fb_id");

                            JSONObject user_info=itemdata.optJSONObject("user_info");
                            item.first_name=user_info.optString("first_name");
                            item.last_name=user_info.optString("last_name");
                            item.profile_pic=user_info.optString("profile_pic");


                            item.video_id=itemdata.optString("id");
                            item.comments=itemdata.optString("comments");
                            item.created=itemdata.optString("created");


                            arrayList.add(item);
                        }

                        api_callBack.ArrayData(arrayList);

                    }else {
                        Toast.makeText(activity, ""+response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }

            }
        });


    }

    public static void Call_Api_For_get_Comment(final Activity activity, String video_id, final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
             parameters.put("video_id",video_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.showVideoComments, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                ArrayList<Comment_Get_Set> arrayList=new ArrayList<>();
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    if(code.equals("200")){
                        JSONArray msgArray=response.getJSONArray("msg");
                        for (int i=0;i<msgArray.length();i++) {
                            JSONObject itemdata = msgArray.optJSONObject(i);
                            Comment_Get_Set item=new Comment_Get_Set();
                            item.fb_id=itemdata.optString("fb_id");

                            JSONObject user_info=itemdata.optJSONObject("user_info");
                            item.first_name=user_info.optString("first_name");
                            item.last_name=user_info.optString("last_name");
                            item.profile_pic=user_info.optString("profile_pic");


                            item.video_id=itemdata.optString("id");
                            item.comments=itemdata.optString("comments");
                            item.created=itemdata.optString("created");


                            arrayList.add(item);
                        }

                        api_callBack.ArrayData(arrayList);

                    }else {
                        Toast.makeText(activity, ""+response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });

    }


    public static void Call_Api_For_update_view(final Activity activity,
                                               String video_id) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("id",video_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity,Variables.updateVideoView, parameters,null);


    }



    public static void Call_Api_For_Follow_or_unFollow
            (final Activity activity,
             String fb_id,
             String followed_fb_id,
            String status,
            final API_CallBack api_callBack) {

        Functions.Show_loader(activity,false,false);


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);
            parameters.put("followed_fb_id",followed_fb_id);
            parameters.put("status",status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

          ApiRequest.Call_Api(activity, Variables.follow_users, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    if(code.equals("200")){
                        api_callBack.OnSuccess(response.toString());

                    }

                    else {
                        Toast.makeText(activity, ""+response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });


    }


    public static void Call_Api_For_Get_User_data
            (final Activity activity,
             String fb_id,
            final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp",parameters.toString());

        ApiRequest.Call_Api(activity, Variables.get_user_data, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    if(code.equals("200")){
                        api_callBack.OnSuccess(response.toString());

                    }

                    else {
                        Toast.makeText(activity, ""+response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });

    }




    public static void Call_Api_For_Delete_Video
            (final Activity activity,
             String video_id,
             final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id", video_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(activity, Variables.DeleteVideo, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    if(code.equals("200")){
                        if(api_callBack!=null)
                            api_callBack.OnSuccess(response.toString());

                    }

                    else {
                        Toast.makeText(activity, ""+response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    if(api_callBack!=null)
                        api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }

            }
        });



    }







    public static Dialog indeterminant_dialog;
    public static void Show_indeterminent_loader(Context context, boolean outside_touch, boolean cancleable) {

        indeterminant_dialog = new Dialog(context);
        indeterminant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        indeterminant_dialog.setContentView(R.layout.item_indeterminant_progress_layout);
        indeterminant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.d_round_white_background));


        if(!outside_touch)
            indeterminant_dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            indeterminant_dialog.setCancelable(false);

        indeterminant_dialog.show();

    }


    public static void cancel_indeterminent_loader(){
        if(indeterminant_dialog!=null){
            indeterminant_dialog.cancel();
        }
    }




    public static Dialog determinant_dialog;
   public static ProgressBar determinant_progress;

    public static void Show_determinent_loader(Context context, boolean outside_touch, boolean cancleable) {

        determinant_dialog = new Dialog(context);
        determinant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        determinant_dialog.setContentView(R.layout.item_determinant_progress_layout);
        determinant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.d_round_white_background));

        determinant_progress=determinant_dialog.findViewById(R.id.pbar);

        if(!outside_touch)
            determinant_dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            determinant_dialog.setCancelable(false);

        determinant_dialog.show();

    }

    public static void Show_loading_progress(int progress){
        if(determinant_progress!=null ){
            determinant_progress.setProgress(progress);

        }
    }


    public static void cancel_determinent_loader(){
        if(determinant_dialog!=null){
            determinant_progress=null;
             determinant_dialog.cancel();
        }
    }


    public static boolean Checkstoragepermision(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;

            } else {

                activity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else {

            return true;
        }
    }


    // these function are remove the cache memory which is very helpfull in memmory managmet
    public static void deleteCache(Context context) {
        Glide.get(context).clearMemory();

        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}


    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}
