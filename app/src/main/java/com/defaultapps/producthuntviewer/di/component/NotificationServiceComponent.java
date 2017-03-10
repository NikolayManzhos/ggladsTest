package com.defaultapps.producthuntviewer.di.component;


import com.defaultapps.producthuntviewer.di.module.NotificationServiceModule;
import com.defaultapps.producthuntviewer.service.notification.NotificationService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NotificationServiceModule.class)
public interface NotificationServiceComponent {
    void inject(NotificationService notificationService);
}
