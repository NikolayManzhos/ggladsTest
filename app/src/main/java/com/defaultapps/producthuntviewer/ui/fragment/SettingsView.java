package com.defaultapps.producthuntviewer.ui.fragment;

import android.os.Bundle;

import android.support.annotation.Nullable;

import com.defaultapps.producthuntviewer.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;


public class SettingsView extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_settings);
    }
}
