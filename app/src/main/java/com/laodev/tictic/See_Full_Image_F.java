package com.laodev.tictic;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class See_Full_Image_F extends Fragment {


    View view;
    Context context;
    ImageButton close_gallery;


    ImageView single_image;

    String image_url;

    ProgressBar p_bar;


    int width,height;

    public See_Full_Image_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_see_full_image, container, false);
        context=getContext();

         DisplayMetrics displayMetrics = new DisplayMetrics();
         getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
         width = displayMetrics.widthPixels;

        image_url=getArguments().getString("image_url");

        close_gallery=view.findViewById(R.id.close_gallery);
        close_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        p_bar=view.findViewById(R.id.p_bar);

        single_image=view.findViewById(R.id.single_image);


            p_bar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(image_url).placeholder(R.drawable.image_placeholder)
                    .into(single_image, new Callback() {
                        @Override
                        public void onSuccess() {

                            p_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            // TODO Auto-generated method stub
                            p_bar.setVisibility(View.GONE);
                        }
                    });



        return view;
    }







}


