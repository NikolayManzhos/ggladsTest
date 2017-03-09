package com.defaultapps.producthuntviewer.di.module;

import android.app.Application;
import android.content.Context;

import com.defaultapps.producthuntviewer.R;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return  application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() { return application.getApplicationContext(); }

    @Provides
    List<String> provideCategoriesList() { return Arrays.asList(application.getResources().getStringArray(R.array.categories)); }
}
