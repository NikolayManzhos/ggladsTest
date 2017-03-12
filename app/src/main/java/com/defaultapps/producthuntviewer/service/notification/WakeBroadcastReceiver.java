package com.defaultapps.producthuntviewer.service.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class WakeBroadcastReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (sharedPreferences.getBoolean("notification", false)) {
            Log.d("WakeBroadcastReceiver", "started");
            Intent notificationIntent = new Intent(context, NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, NotificationService.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Long.parseLong(sharedPreferences.getString("notificationTime", "3600000")), pendingIntent);
        }
    }
}
