package com.defaultapps.producthuntviewer.service.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.local.LocalService;
import com.defaultapps.producthuntviewer.data.local.sp.SharedPreferencesHelper;
import com.defaultapps.producthuntviewer.data.local.sp.SharedPreferencesManager;
import com.defaultapps.producthuntviewer.data.model.post.PostsResponse;
import com.defaultapps.producthuntviewer.data.net.NetworkService;
import com.defaultapps.producthuntviewer.ui.activity.MainActivity;

import java.io.IOException;

public class NotificationService extends IntentService {

    public static final int NOTIFICATION_ID = 2911;

    //TODO: Notifications
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    private void fillNotificationWithData() {
        LocalService localService = new LocalService(getApplicationContext());
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(new SharedPreferencesHelper(getApplicationContext()));
        NetworkService networkService = new NetworkService();
        try {
            PostsResponse response = localService.readResponseFromFile();
            int lastSize = response.getPosts().size();
            retrofit2.Response<PostsResponse> networkResponse = networkService.getNetworkCall().getPosts(getResources().getString(R.string.PRODUCT_HUNT_KEY), sharedPreferencesManager.getCategory()).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("")
                .setContentText("")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
