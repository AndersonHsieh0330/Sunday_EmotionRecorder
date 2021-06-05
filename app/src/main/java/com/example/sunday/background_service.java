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
    private final int notificationmanager_id = 1;
    private boolean is_service_bounded_service;//status tracking
    private boolean is_emotion_ready_service;//status tracking
    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("emotion_notready_from_notification".equals(intent.getAction())) {
                if (intent.getBooleanExtra("Notification_pressed", false)) {
                    SharedPreferences sp = context.getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
                    cooldown_cycle(sp.getInt("Latency", 0));
                }
            }
        }
    };

    private final IBinder mybinder = new localbinder();
    public class localbinder extends Binder {
        background_service getService() {
            return background_service.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getApplicationContext().getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
        speditor = sp.edit();
        notificationManager = NotificationManagerCompat.from(this);

        IntentFilter filter = new IntentFilter("emotion_notready_from_notification");
        registerReceiver(broadcastReceiver, filter);
    }


    @Override
    public IBinder onBind(Intent intent) {
        is_service_bounded_service = true;
        return mybinder;

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        is_service_bounded_service = false;
        return super.onUnbind(intent);
    }

    public void cooldown_cycle(int latency) {
        is_emotion_ready_service = false;

        CountDownTimer countDownTimer = new CountDownTimer(latency, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                speditor.putBoolean("Emotion_ready", false);
                speditor.commit();

            }

            @Override
            public void onFinish() {
                is_emotion_ready_service = true;
                Intent intent = new Intent();
                intent.setAction("emotion_actionfilter");
                intent.putExtra("emotion_ready", true);
                sendBroadcast(intent);

                if (sp.getBoolean("Notification_onoroff", false)) {
                    send_notifications();
                }

                speditor.putBoolean("Emotion_ready", true);
                speditor.commit();
            }
        };

        countDownTimer.start();
    }

    public void send_notifications() {
        Intent notification_intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notification_intent, 0);

        Intent broadcast_intent_happy = new Intent(this, Notification_receiver_happy.class);
        broadcast_intent_happy.putExtra("emotion", 1);
        PendingIntent action_intent_happy = PendingIntent.getBroadcast(this, 0, broadcast_intent_happy, PendingIntent.FLAG_ONE_SHOT);

        Intent broadcast_intent_sad = new Intent(this, Notification_receiver_happy.class);
        broadcast_intent_sad.putExtra("emotion", 2);
        PendingIntent action_intent_sad = PendingIntent.getBroadcast(this, 1, broadcast_intent_sad, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, notification_channel.noti_channel_ID)
                .setSmallIcon(R.drawable.lightbulb_default)
                .setContentTitle("Sunday")
                .setContentText("How's your day going?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.lightbulb_bad_painted, "Bad..", action_intent_sad)
                .addAction(R.drawable.lightbulb_default, "Good!", action_intent_happy)
                .build();

        notificationManager.notify(notificationmanager_id, notification);

    }


}
