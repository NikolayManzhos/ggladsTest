package com.defaultapps.producthuntviewer.service.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.local.LocalService;
import com.defaultapps.producthuntviewer.data.local.sp.SharedPreferencesManager;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.defaultapps.producthuntviewer.data.model.post.PostsResponse;
import com.defaultapps.producthuntviewer.data.net.NetworkService;
import com.defaultapps.producthuntviewer.di.component.DaggerNotificationServiceComponent;
import com.defaultapps.producthuntviewer.di.module.NotificationServiceModule;
import com.defaultapps.producthuntviewer.ui.activity.MainActivity;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;

public class NotificationService extends IntentService {

    public static final int NOTIFICATION_ID = 2911;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    NetworkService networkService;

    @Inject
    LocalService localService;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerNotificationServiceComponent.builder()
                .notificationServiceModule(new NotificationServiceModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("NotificationService", "intent received");
        fillNotificationWithData();
    }

    private void fillNotificationWithData() {
        try {
            PostsResponse response = localService.readResponseFromFile();
            int lastSize = response.getPosts().size();
            Response<PostsResponse> retrofitResponse = networkService.getNetworkCall().getPosts(getResources().getString(R.string.PRODUCT_HUNT_KEY), sharedPreferencesManager.getCategory()).execute();
            PostsResponse networkResponse = retrofitResponse.body();
            localService.writeResponseToFile(networkResponse);
            if ((networkResponse.getPosts().size() - lastSize) == 1) {
                Post latestProduct = networkResponse.getPosts().get(networkResponse.getPosts().size() - 1);
                showNotification("New product available", latestProduct.getName() + " with " + latestProduct.getVotesCount() + " UpVotes!");
            } else if ((networkResponse.getPosts().size() - lastSize) > 1) {
                showNotification(String.valueOf(networkResponse.getPosts().size() - lastSize) + " new products available", "Check it out!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showNotification(String title, String description) {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
