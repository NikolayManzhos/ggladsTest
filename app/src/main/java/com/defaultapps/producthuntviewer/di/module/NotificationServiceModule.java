package com.defaultapps.producthuntviewer.di.module;


import android.content.Context;

import com.defaultapps.producthuntviewer.service.notification.NotificationService;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationServiceModule {
    NotificationService notificationService;

    public NotificationServiceModule(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Provides
    Context provideContext() { return notificationService.getApplicationContext(); }
}
