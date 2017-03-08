package com.defaultapps.producthuntviewer.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.defaultapps.producthuntviewer.App;
import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.local.sp.SharedPreferencesManager;
import com.defaultapps.producthuntviewer.ui.fragment.ProductDescriptionViewImpl;
import com.defaultapps.producthuntviewer.ui.fragment.ProductsListViewImpl;
import com.defaultapps.producthuntviewer.ui.fragment.SettingsView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ProductsListViewImpl.OnPostClickCallback {

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent(this).inject(this);
        if (!sharedPreferencesManager.getFirstTimeLaunch()) {
            sharedPreferencesManager.setCategory("tech");
            sharedPreferencesManager.setFirstTimeUser(true);
        }

        if (savedInstanceState == null) {
            replaceFragment(new ProductsListViewImpl());
        }
    }

    @Override
    public void onPostClick() {
        replaceFragment(new ProductDescriptionViewImpl());
    }

    @Override
    public void onSettingsClick() {
//        replaceFragment(new SettingsView());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.contentFrame, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
