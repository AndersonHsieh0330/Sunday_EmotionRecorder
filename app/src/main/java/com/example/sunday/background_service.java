package com.example.sunday;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class background_service extends Service {
    private NotificationManagerCompat notificationManager;
    private int notificationmanager_id = 1;
    private boolean is_service_bounded_service;//keep track in both lightbulbfragment and service
    private boolean is_emotion_ready_service;//keep track in both lightbulbfragment and service
    private int current_count_down;
    public boolean isIs_emotion_ready_service() {
        return is_emotion_ready_service;
    }


    public int getCurrent_count_down() {
        return current_count_down;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("emotion_notready_from_notification".equals(intent.getAction())){

                if(intent.getBooleanExtra("Notification_pressed",false)){
                    SharedPreferences sp = context.getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
                    cooldown_cycle(sp.getInt("Latency", 0));
//                    Intent lightbulb_status_notification_inetnt = new Intent();
//                    lightbulb_status_notification_inetnt.setAction("emotion_actionfilter");
//                    lightbulb_status_notification_inetnt.putExtra("light_status_count",intent.getIntExtra("light_status_count",3) );
//                    sendBroadcast(lightbulb_status_notification_inetnt);
                }
            }
        }
    };

    private final IBinder mybinder = new localbinder();
    public class localbinder extends Binder{
        background_service getService(){
            return background_service.this;
            /////////////// 這裡的bindService.this return的是甚麼? 是外部class的物件嗎?
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);

        IntentFilter filter= new IntentFilter("emotion_notready_from_notification");
        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        is_service_bounded_service = true;
        return mybinder;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        is_service_bounded_service = false;
        return super.onUnbind(intent);
    }

    public void cooldown_cycle(int latency){
        is_emotion_ready_service = false;
        current_count_down = 5000;

        CountDownTimer countDownTimer = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                current_count_down = current_count_down -1000;
                Intent broadcast = new Intent();
                broadcast.setAction("emotion_actionfilter");
                broadcast.putExtra("emotion_ready",false);
                sendBroadcast(broadcast);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setAction("emotion_actionfilter");
                intent.putExtra("emotion_ready",true);
                sendBroadcast(intent);
                is_emotion_ready_service = true;

                SharedPreferences sp = getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
                if(sp.getBoolean("Notification_onoroff",false)){
                    send_notifications();
                }
            }
        };

        countDownTimer.start();
    }

    public void send_notifications(){
        Intent notification_intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notification_intent,0);

        Intent broadcast_intent_happy = new Intent(this,Notification_receiver_happy.class);
        broadcast_intent_happy.putExtra("emotion", 1);
        PendingIntent action_intent_happy = PendingIntent.getBroadcast(this,0,broadcast_intent_happy,PendingIntent.FLAG_ONE_SHOT);

        Intent broadcast_intent_sad = new Intent(this,Notification_receiver_happy.class);
        broadcast_intent_sad.putExtra("emotion", 2);
        PendingIntent action_intent_sad = PendingIntent.getBroadcast(this,1,broadcast_intent_sad,PendingIntent.FLAG_ONE_SHOT);
        //flag decides what happen when we recreate this pendingintent with a new intent


        Notification notification = new NotificationCompat.Builder(this, notification_channel.noti_channel_ID)
                .setSmallIcon(R.drawable.lightbulb_default)
                .setContentTitle("Sunday")
                .setContentText("How's your day going?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.lightbulb_bad_painted, "Bad..", action_intent_sad)
                .addAction(R.drawable.lightbulb_default,"Good!", action_intent_happy)
                .build();

        notificationManager.notify(notificationmanager_id,notification);

    }


}
