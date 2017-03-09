package com.defaultapps.producthuntviewer.data.local.sp;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SharedPreferencesManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private final String CACHE_TIME = "cache_exp_time";
    private final String CATEGORY_CODE = "category_code";
    private final String CATEGORY_NUMB = "category_number";
    private final String FORCE_LOAD = "force_load";
    private final String FIRST_TIME_USER = "first_time";

    @Inject
    public SharedPreferencesManager(SharedPreferencesHelper sharedPreferencesHelper) {
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    public long getCacheTime() {
        return sharedPreferencesHelper.getLong(CACHE_TIME);
    }

    public void setCacheTime(long currentTime) {
        sharedPreferencesHelper.putLong(CACHE_TIME, currentTime);
    }

    public String getCategory() {
        return sharedPreferencesHelper.getString(CATEGORY_CODE);
    }

    public void setCategory(String sourceCode) {
        sharedPreferencesHelper.putString(CATEGORY_CODE, sourceCode);
    }

    public int getCategoryNumber() {
        return sharedPreferencesHelper.getInt(CATEGORY_NUMB);
    }

    public void setCategoryNumber(int categoryNumber) {
        sharedPreferencesHelper.putInt(CATEGORY_NUMB, categoryNumber);
    }

    public boolean getForceLoadStatus() {
        return sharedPreferencesHelper.getBoolean(FORCE_LOAD);
    }

    public void setForceLoadStatus(boolean loadStatus) {
        sharedPreferencesHelper.putBoolean(FORCE_LOAD, loadStatus);
    }

    public boolean getFirstTimeLaunch() {
        return sharedPreferencesHelper.getBoolean(FIRST_TIME_USER);
    }

    public void setFirstTimeUser(boolean firstTimeUser) {
        sharedPreferencesHelper.putBoolean(FIRST_TIME_USER, firstTimeUser);
    }

}
