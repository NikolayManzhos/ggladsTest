package com.defaultapps.producthuntviewer;

import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.defaultapps.producthuntviewer.ui.activity.MainActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

import android.support.test.espresso.contrib.RecyclerViewActions;

import junit.framework.AssertionFailedError;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class UiTests {
    @Rule
    public ActivityTestRule<MainActivity> mNotesActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test1MainFunctionalityTest() throws Exception {
        onView(withId(R.id.swipeRefreshProductsList)).perform(swipeDown());

        //wait for data
        Thread.sleep(2500);

        onView(withId(R.id.productsRecycler)).check(matches(isDisplayed()));
        onView(withId(R.id.productsRecycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewAction.clickChildViewWithId(R.id.productItem)));
        onView(withId(R.id.screenshot)).check(matches(isDisplayed()));

        onView(withId(R.id.getProduct)).perform(click());
        onView(withId(R.id.webView)).check(matches(isDisplayed()));
    }

    @Test
    public void test2Settings() throws Exception {
        onView(withId(R.id.settings)).perform(click());
        try {
            onData(PreferenceMatchers.withKey("notification")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("notificationTime")).check(matches(not(isEnabled())));
        } catch (AssertionFailedError e) {
            onData(PreferenceMatchers.withKey("notificationTime")).check(matches(isEnabled()));
        }
    }
}
