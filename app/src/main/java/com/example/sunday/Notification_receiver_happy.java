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
    private final int notificationmanager_id = 1;//this is the id when I did notificationmanager.notify(id)
    private NotificationManagerCompat notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        sqlitehelper =  new dbhelper(context);
        notificationManager = NotificationManagerCompat.from(context);
        emotion_node node = new emotion_node(Calendar.getInstance(),intent.getIntExtra("emotion",3));
        Log.d("notireceiver", String.valueOf(intent.getIntExtra("emotion",3)));
        sqlitehelper.add_data(node);
        notificationManager.cancel(notificationmanager_id);

        Intent broadcast_intent = new Intent();
        broadcast_intent.setAction("emotion_notready_from_notification");
        broadcast_intent.putExtra("Notification_pressed",true);
        context.sendBroadcast(broadcast_intent);

        Intent count_status_intent = new Intent();
        count_status_intent.setAction("emotion_actionfilter");
        count_status_intent.putExtra("light_status_count",intent.getIntExtra("emotion",3));
        context.sendBroadcast(count_status_intent);
    }
}
