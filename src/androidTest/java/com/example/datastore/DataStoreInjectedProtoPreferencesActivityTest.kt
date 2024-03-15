package com.example.datastore

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DataStoreInjectedProtoPreferencesActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testDataStoreInjectedProtoPreferencesActivity() {
        launchActivity<DataStoreInjectedProtoPreferencesActivity>().use {
            onView(withId(R.id.clear)).perform(click())
            onView(withId(R.id.user_name)).check(matches(withText("")))
            onView(withId(R.id.user_email)).check(matches(withText("")))
            onView(withId(R.id.status)).check(matches(withText("Cleared")))
            onView(withId(R.id.user_name)).perform(typeText("Hello"))
            onView(withId(R.id.user_email)).perform(typeText("World"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.save)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Saved")))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.clear)).perform(click())
            onView(withId(R.id.user_name)).check(matches(withText("")))
            onView(withId(R.id.user_email)).check(matches(withText("")))
            onView(withId(R.id.status)).check(matches(withText("Cleared")))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.load_old_prefs)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Loaded")))
            onView(withId(R.id.user_name)).check(matches(withText("Hello")))
            onView(withId(R.id.user_email)).check(matches(withText("World")))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.clear)).perform(click())
            onView(withId(R.id.load)).perform(click())
            onView(withId(R.id.user_name)).check(matches(withText("Hello")))
            onView(withId(R.id.user_email)).check(matches(withText("World")))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.clear)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Cleared")))
            onView(withId(R.id.user_name)).check(matches(withText("")))
            onView(withId(R.id.user_email)).check(matches(withText("")))
            onView(withId(R.id.user_name)).perform(typeText("World"))
            onView(withId(R.id.user_email)).perform(typeText("Hello"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.save_datastore)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Saved")))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.load)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Loaded")))
            onView(withId(R.id.user_name)).check(matches(withText("World")))
            onView(withId(R.id.user_email)).check(matches(withText("Hello")))
        }
    }
}
