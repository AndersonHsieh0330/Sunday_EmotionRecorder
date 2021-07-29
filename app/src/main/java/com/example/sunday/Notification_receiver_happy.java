package com.example.sunday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class Notification_receiver_happy extends BroadcastReceiver {
    dbhelper sqlitehelper;
    private static final int notificationmanager_id = 1;//this is the id when I did notificationmanager.notify(id)
    public static final String ACTION_BUTTON_TAG = "emotion";
    private NotificationManagerCompat notificationManager;
    public static final String EMOTION_NOT_READY_INTENTACTION = "emotion_notready_from_notification";
    public static final String IS_NOTIFICATION_ACTIONBUTTON_PRESSED_TAG = "Notification_pressed";
    public static final String EMOTION_ACTION_FILTER = "emotion_actionfilter";
    public static final String LIGHTBULB_STATE_COUNT_TAG = "light_status_count";
    @Override
    public void onReceive(Context context, Intent intent) {
        sqlitehelper =  new dbhelper(context);
        notificationManager = NotificationManagerCompat.from(context);
        emotion_node node = new emotion_node(Calendar.getInstance(),intent.getIntExtra("emotion",3));
        sqlitehelper.add_data(node);
        notificationManager.cancel(notificationmanager_id);

        context.sendBroadcast(create_Notification_Pressed_Broadcast());
        context.sendBroadcast(create_Lightbulb_Count_Broadcast(intent));
    }

    private Intent create_Notification_Pressed_Broadcast(){
        Intent broadcast_intent = new Intent();
        broadcast_intent.setAction(EMOTION_NOT_READY_INTENTACTION);
        broadcast_intent.putExtra(IS_NOTIFICATION_ACTIONBUTTON_PRESSED_TAG,true);
       return broadcast_intent;
    }
    private Intent create_Lightbulb_Count_Broadcast(Intent Intent_Received){
        Intent count_status_intent = new Intent();
        count_status_intent.setAction(EMOTION_ACTION_FILTER);
        count_status_intent.putExtra(LIGHTBULB_STATE_COUNT_TAG,Intent_Received.getIntExtra("emotion",3));
        return count_status_intent;
    }

}
