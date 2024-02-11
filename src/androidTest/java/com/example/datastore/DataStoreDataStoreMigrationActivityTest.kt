package com.example.datastore

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreDataStoreMigrationActivityTest {

    @Test
    fun testDataStoreDataStoreMigrationActivity() {
        launchActivity<DataStoreDataStoreMigrationActivity>().use {
            onView(withId(R.id.value)).check(matches(withText("")))
            onView(withId(R.id.value)).perform(typeText("3.14"))
            onView(withId(R.id.save_old_data)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Saved")))
            onView(withId(R.id.load_migrated_data)).perform(click())
            onView(withId(R.id.status)).check(matches(withText("Loaded")))
            onView(withId(R.id.value)).check(matches(withText("3")))
        }
    }
}
