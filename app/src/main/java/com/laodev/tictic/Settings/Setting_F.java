package com.laodev.tictic.Settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laodev.tictic.Main_Menu.MainMenuActivity;
import com.laodev.tictic.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.laodev.tictic.R;
import com.laodev.tictic.SimpleClasses.Variables;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setting_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    public Setting_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_setting, container, false);


        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.logout_txt).setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.logout_txt:
                Logout();
                break;
        }
    }



    // this will erase all the user info store in locally and logout the user
    public void Logout(){
        SharedPreferences.Editor editor= Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id,"").clear();
        editor.putString(Variables.u_name,"").clear();
        editor.putString(Variables.u_pic,"").clear();
        editor.putBoolean(Variables.islogin,false).clear();
        editor.commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainMenuActivity.class));
    }


}
