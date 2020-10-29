package com.laodev.tictic.Notifications;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laodev.tictic.Inbox.Inbox_F;
import com.laodev.tictic.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.laodev.tictic.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    Notification_Adapter adapter;
    RecyclerView recyclerView;

    ArrayList<Notification_Get_Set> datalist;


    public Notification_F() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notification, container, false);
        context=getContext();


        datalist=new ArrayList<>();


        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        adapter=new Notification_Adapter(context, datalist, new Notification_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Notification_Get_Set item) {

            }
        }
    );

        recyclerView.setAdapter(adapter);



        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);

        view.findViewById(R.id.inbox_btn).setOnClickListener(this);

        return view;
    }


    AdView adView;
    @Override
    public void onStart() {
        super.onStart();
        adView = view.findViewById(R.id.bannerad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inbox_btn:
                Open_inbox_F();
                break;
        }
    }

    private void Open_inbox_F() {

        Inbox_F inbox_f = new Inbox_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, inbox_f).commit();

    }


}
