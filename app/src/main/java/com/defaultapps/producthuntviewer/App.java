package com.defaultapps.producthuntviewer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.defaultapps.producthuntviewer.di.component.AppComponent;
import com.defaultapps.producthuntviewer.di.component.DaggerAppComponent;
import com.defaultapps.producthuntviewer.di.module.AppModule;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;


public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify
                .with(new MaterialModule());
    }

    @VisibleForTesting
    protected AppComponent createComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent(Context context) {
        App app = (App) context.getApplicationContext();
        if (app.component == null) {
            app.component = app.createComponent();
        }
        return app.component;
    }
}
