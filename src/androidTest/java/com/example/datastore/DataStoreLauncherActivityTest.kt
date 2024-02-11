package com.example.datastore

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DataStoreLauncherActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var scenario: ActivityScenario<DataStoreLauncherActivity>

    @Before
    fun setUp() {
        scenario = launchActivity()
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun testOpenDataStorePreferences() {
        onView(withId(R.id.datastore_preferences)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStorePreferencesActivity::class.java.name))
    }

    @Test
    fun testOpenDataStorePreferencesMigration() {
        onView(withId(R.id.datastore_preferences_migration)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStorePreferencesMigrationActivity::class.java.name))
    }

    @Test
    fun testOpenDataStoreProtoPreferences() {
        onView(withId(R.id.datastore_proto_preferences)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStoreProtobufActivity::class.java.name))
    }

    @Test
    fun testOpenDataStoreInjectedPreferences() {
        onView(withId(R.id.injected_data_store)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStoreInjectedPreferencesActivity::class.java.name))
    }

    @Test
    fun testOpenDataStoreInjectedProtoPreferences() {
        onView(withId(R.id.injected_proto_data_store)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStoreInjectedProtoPreferencesActivity::class.java.name))
    }

    @Test
    fun testOpenDataStoreJsonPreferences() {
        onView(withId(R.id.json_data_store)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStoreJsonPreferencesActivity::class.java.name))
    }

    @Test
    fun testOpenDataStoreDataStoreMigration() {
        onView(withId(R.id.data_store_data_store_migration)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DataStoreDataStoreMigrationActivity::class.java.name))
    }
}
