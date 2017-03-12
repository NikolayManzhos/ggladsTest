package com.defaultapps.producthuntviewer.di.component;

import com.defaultapps.producthuntviewer.di.module.AppModule;
import com.defaultapps.producthuntviewer.ui.activity.MainActivity;
import com.defaultapps.producthuntviewer.ui.fragment.ProductsListViewImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(ProductsListViewImpl productsListViewImpl);
    void inject(MainActivity mainActivity);
}
