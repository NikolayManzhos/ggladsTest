package com.defaultapps.producthuntviewer.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.service.notification.NotificationService;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        getFragmentManager().beginTransaction().replace(R.id.contentFrame, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {

        private SwitchPreference notificationStatus;
        private ListPreference hours;

        private Context context;
        private SharedPreferences sharedPreferences;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_settings);
            context = getActivity().getApplicationContext();

            notificationStatus = (SwitchPreference) getPreferenceManager().findPreference("notification");
            hours = (ListPreference) getPreferenceManager().findPreference("notificationTime");

            notificationStatus.setOnPreferenceChangeListener(this);
            if (notificationStatus.isChecked()) {
                hours.setEnabled(false);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Intent intent = new Intent(getActivity(), NotificationService.class);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            PendingIntent pendingIntent = PendingIntent.getService(context, NotificationService.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            String test = sharedPreferences.getString("notificationTime", "3600000");
            Log.d("TEST", test);
            if ((boolean) newValue) {
                alarmManager.cancel(pendingIntent);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),  Long.valueOf(sharedPreferences.getString("notificationTime", "3600000")).longValue() , pendingIntent);
                hours.setEnabled(false);
            } else {
                alarmManager.cancel(pendingIntent);
                hours.setEnabled(true);
            }
            return true;
        }
    }
}
