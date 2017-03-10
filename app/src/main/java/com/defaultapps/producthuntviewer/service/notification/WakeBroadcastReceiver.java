package com.defaultapps.producthuntviewer.service.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class WakeBroadcastReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean("notifications", false)) {
            Intent notificationIntent = new Intent(context, NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, NotificationService.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Long.getLong(sharedPreferences.getString("notificationTime", "3600000")).longValue(), pendingIntent);
        }
    }
}
