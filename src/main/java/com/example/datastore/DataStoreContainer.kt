package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException

/**
 * Property extension for data store preferences
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "datastore_prefs"
)

const val PREF_USER_NAME = "config_user_name"
const val PREF_USER_LAST_NAME = "config_user_last_name"

val KEY_CONFIG_USER_NAME = stringPreferencesKey(PREF_USER_NAME)
val KEY_CONFIG_USER_LAST_NAME = stringPreferencesKey(PREF_USER_LAST_NAME)

const val KEY_SHARD_PREFERENCES_FILE = "app_shared_preferences"

val Context.migratedDataStore: DataStore<Preferences> by preferencesDataStore(
    name = KEY_SHARD_PREFERENCES_FILE,
    produceMigrations = { context ->
        // Since we're migrating from SharedPreferences, add migration based on the
        // SharedPreferences name
        listOf(SharedPreferencesMigration(context, KEY_SHARD_PREFERENCES_FILE))
    }
)

const val KEY_PROTO_SHARD_PREFERENCES_FILE = "app_proto_shared_preferences"
const val KEY_INJECTED_PROTO_SHARD_PREFERENCES_FILE = "app_injected_proto_shared_preferences"

/**
 * Property extension for data store preferences which work with Proto Buffers
 */
val Context.protoDataStore: DataStore<Credentials> by dataStore(
    fileName = KEY_PROTO_SHARD_PREFERENCES_FILE,
    serializer = CredentialsSerializer,
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                KEY_PROTO_SHARD_PREFERENCES_FILE
            ) { sharedPrefs: SharedPreferencesView, credentials: Credentials ->
                val type = if (sharedPrefs.getBoolean("KEY_ADMIN", false)) {
                    Credentials.UserType.ADMIN
                } else {
                    Credentials.UserType.REGULAR
                }
                credentials.copy {
                    userName = sharedPrefs.getString(PREF_USER_NAME) ?: throw IOException("Failed to migrate")
                    userEmail = sharedPrefs.getString(PREF_USER_LAST_NAME) ?: throw IOException("Failed to migrate")
                    userType = type
                }
            }
        )
    }
)
