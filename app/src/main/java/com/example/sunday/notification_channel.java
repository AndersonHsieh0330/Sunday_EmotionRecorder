package com.example.sunday;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class notification_channel extends Application {
    public static final String noti_channel_ID = "emotion_channel_id";
    @Override
    public void onCreate() {
        super.onCreate();

        create_notification_channel();
    }


    public void create_notification_channel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(noti_channel_ID, "emotion_reminder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("channel for Sunday emotion recorder");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
