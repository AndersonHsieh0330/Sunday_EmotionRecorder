package com.example.sunday;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class lightbulbfragment extends Fragment {
    private RecyclerView lightbulb_recycleview;
    private RecyclerView.LayoutManager mylayoutmanager;
    private LinkedList<emotion_node> recycle_linkedlist = new LinkedList<emotion_node>();
    private lightbulb_adapter adapter;
    private int lightbulb_icon_count = 0;
    private ImageButton light_imagebutton;
    private ImageButton add_button;
    private dbhelper sqlitehelper;
    private SharedPreferences sp;
    private int latency_lightfrag;
    private background_service currentservice;
    private boolean is_service_bounded_lightfrag;//keep track in both lightbulbfragment and service
    private boolean is_emotion_ready_lightfrag;//keep track in both lightbulbfragment and service
    ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            background_service.localbinder binder = (background_service.localbinder)service;
            //把argument裡面這一個IBinder的物件還原為剛剛在bindService class裡面製作的local binder class
            //然後剛剛localbinder class裡面有唯一的getService() method
            //此method回傳我剛剛寫的service
            currentservice = binder.getService();
            //這裡currentservice的declaration已經被定義在外面了 所以整個Mainactivity class都可以使用這個currentservice裡面的東西
            is_service_bounded_lightfrag = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    BroadcastReceiver messagereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("emotion_actionfilter".equals(intent.getAction())){
                if(intent.getBooleanExtra("emotion_ready",false)){
                    is_emotion_ready_lightfrag = true;
                    light_imagebutton.setEnabled(true);
                    light_imagebutton.setBackgroundResource(R.drawable.lightbulb_default);
                    lightbulb_icon_count = 0;
                    add_button.setEnabled(true);

                }else{
                    is_emotion_ready_lightfrag = false;
                    light_imagebutton.setEnabled(false);
                    add_button.setEnabled(false);
                }

                if(intent.getIntExtra("light_status_count",3)==1){
                    lightbulb_icon_count = 1;
                    light_imagebutton.setBackgroundResource(R.drawable.lightbulb_good);
                }else if(intent.getIntExtra("light_status_count",3)==2){
                    lightbulb_icon_count = 2;
                    light_imagebutton.setBackgroundResource(R.drawable.lightbulb_bad_painted);
                }

            }


        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getActivity().unregisterReceiver(messagereceiver);
        this.getActivity().unbindService(mconnection);

        Intent intent = new Intent(this.getActivity(), background_service.class);
        this.getActivity().startService(intent);
        is_service_bounded_lightfrag = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lightbulb, container, false);



        //////////////////////////bind to service///////////////////////
        Intent intent = new Intent(this.getContext(),background_service.class);
        sp = getActivity().getApplicationContext().getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
        if(!sp.contains("Notification_onoroff")){
            sp.edit().putBoolean("Notification_onoroff",true).commit();
        }
        latency_lightfrag = sp.getInt("Latency", 0);
        intent.putExtra("Latency", latency_lightfrag);
        this.getActivity().bindService(intent,mconnection,Context.BIND_AUTO_CREATE);
        is_service_bounded_lightfrag = true;


        //////////////////initial check////////////////////////////
        sqlitehelper = new dbhelper(this.getActivity());
        recycle_linkedlist = sqlitehelper.view_data();

        /////////////////////////////stop the service////////////////////
        Intent stop_intent = new Intent(this.getActivity(),background_service.class);
        this.getActivity().stopService(stop_intent);


        ////////////////////////register receiver/////////////////////
        IntentFilter filter = new IntentFilter("emotion_actionfilter");
        this.getActivity().registerReceiver(messagereceiver,filter);


        ///////////////////////variable definitions/////////////////
        lightbulb_recycleview = v.findViewById(R.id.lightbulb_list);
        mylayoutmanager = new LinearLayoutManager(getActivity());
        adapter = new lightbulb_adapter();


        ///////////////////////Recycle View/////////////////////////
        lightbulb_recycleview.setHasFixedSize(true);
        lightbulb_recycleview.setLayoutManager(mylayoutmanager);
        lightbulb_recycleview.setAdapter(adapter);


        ////////////////////////get title clock//////////////////////
        TextClock textClock = v.findViewById(R.id.textclock);
        textClock.setFormat12Hour(null);
        textClock.setFormat24Hour("yyyy MMM dd HH:mm");

        ////////////////////////define lightbult_button usage////////////////
        light_imagebutton = v.findViewById(R.id.lightbulb_button);
        View.OnClickListener listen_1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotion_changes();
            }
        };
        light_imagebutton.setOnClickListener(listen_1);
        light_imagebutton.setEnabled(sp.getBoolean("Emotion_ready",true));

        if(sp.getBoolean("Emotion_ready",true)){
            light_imagebutton.setBackgroundResource(R.drawable.lightbulb_default);
        }else{
        if(sp.getInt("latest_emotion_count",0)==1){
            light_imagebutton.setBackgroundResource(R.drawable.lightbulb_good);
        }else if(sp.getInt("latest_emotion_count",0)==2){
            light_imagebutton.setBackgroundResource(R.drawable.lightbulb_bad_painted);
        }}


        /////////////////////define add_button usage//////////////////////
        add_button = v.findViewById(R.id.add_icon);
        View.OnClickListener listen_2 = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add_emotion_element();
            }
        };
        add_button.setOnClickListener(listen_2);
        add_button.setEnabled(sp.getBoolean("Emotion_ready",true));

        ///////////////////check when emotion is not ready but countdown is not on///////////////////

        if(sp.getBoolean("mainact_ondestroy_was_called",false)==true){
            add_button.setEnabled(true);
            light_imagebutton.setEnabled(true);
            light_imagebutton.setBackgroundResource(R.drawable.lightbulb_default);
            lightbulb_icon_count=0;
            is_emotion_ready_lightfrag=true;
            Toast.makeText(this.getContext(),"Please Keep Sunday Opened",Toast.LENGTH_LONG).show();
            sp.edit().putBoolean("mainact_ondestroy_was_called",false).commit();
        }

        return v;
    }

    private void emotion_changes() {
        if (lightbulb_icon_count == 2) {
            lightbulb_icon_count = 0;
        }
        lightbulb_icon_count = lightbulb_icon_count + 1;
        if (lightbulb_icon_count == 1) {
            light_imagebutton.setBackgroundResource(R.drawable.lightbulb_good);
        } else if (lightbulb_icon_count == 2) {
            light_imagebutton.setBackgroundResource(R.drawable.lightbulb_bad_painted);

        }
    }

    private void add_emotion_element() {
        Calendar calen = Calendar.getInstance();
        SharedPreferences.Editor speditor= sp.edit();

        if (lightbulb_icon_count == 1) {
            light_imagebutton.setEnabled(false);
            emotion_node node = new emotion_node(calen, lightbulb_icon_count);
            sqlitehelper.add_data(node);

            recycle_linkedlist = sqlitehelper.view_data();
            adapter.notifyDataSetChanged();
            currentservice.cooldown_cycle(sp.getInt("Latency", 0));
            add_button.setEnabled(false);
            light_imagebutton.setEnabled(false);
            is_emotion_ready_lightfrag =false;


            speditor.putInt("latest_emotion_count",lightbulb_icon_count);
            speditor.commit();
        } else if (lightbulb_icon_count == 2) {
            light_imagebutton.setEnabled(false);

            emotion_node node = new emotion_node(calen, lightbulb_icon_count);
            sqlitehelper.add_data(node);

            recycle_linkedlist = sqlitehelper.view_data();
            adapter.notifyDataSetChanged();
            currentservice.cooldown_cycle(sp.getInt("Latency", 0));
            speditor.putInt("latest_emotion_count",lightbulb_icon_count);
            speditor.commit();
            add_button.setEnabled(false);
            light_imagebutton.setEnabled(false);
            is_emotion_ready_lightfrag =false;

        } else {
            Toast.makeText(this.getActivity(), "Click on lightbult to select a emotion", Toast.LENGTH_SHORT).show();
        }





    }

    ////////////////////////////////////recycle view adapter!!!////////////////////////////////////////////
    class lightbulb_adapter extends RecyclerView.Adapter<lightbulb_adapter.lightbulb_viewholder> {
        SimpleDateFormat element_format = new SimpleDateFormat("HH:mm");

        class lightbulb_viewholder extends RecyclerView.ViewHolder {

            public View itemview;
            public TextView time;//this is the date section in the elements
            public ImageView hap_or_ang;//this is the lightbult icons in the elements

            public lightbulb_viewholder(@NonNull View viewer) {
                super(viewer);
                itemview = viewer;

                time = viewer.findViewById(R.id.recycleitem_date);
                hap_or_ang = viewer.findViewById(R.id.recycleitem_icon);
                ///////2021/2/28 stopped here///////// youtube video 5:54
            }
        }

        @NonNull
        @Override
        public lightbulb_adapter.lightbulb_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lightbulb_recycle_item, parent, false);

            lightbulb_viewholder lightvh = new lightbulb_viewholder(view);

            return lightvh;

        }

        @Override
        public void onBindViewHolder(@NonNull lightbulb_adapter.lightbulb_viewholder holder, int position) {
            holder.time.setText(element_format.format(recycle_linkedlist.get(position).getCal().getTime()));
            holder.hap_or_ang.setImageResource(recycle_linkedlist.get(position).getEmotion_icon());
        }

        @Override
        public int getItemCount() {
            return recycle_linkedlist.size();

        }
    }

}






















