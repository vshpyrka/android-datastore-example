package com.example.datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStoreDataStoreMigrationBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DataStoreDataStoreMigrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStoreDataStoreMigrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveOldData.setOnClickListener {
            lifecycleScope.launch {
                oldDataStore.edit {
                    it[KEY_OLD_VALUE] = binding.value.text.toString().toDouble()
                    it[KEY_ANOTHER_VALUE] = 2.54
                }
                binding.status.text = "Saved"
            }
        }
        binding.loadMigratedData.setOnClickListener {
            lifecycleScope.launch {
                val prefs = newDataStore.data.first()
                val newValue = prefs[KEY_NEW_VALUE]
                binding.value.setText(newValue.toString())
                binding.status.text = "Loaded"
            }
        }
    }
}

val KEY_OLD_VALUE = doublePreferencesKey("KEY_OLD_VALUE")
val KEY_ANOTHER_VALUE = doublePreferencesKey("KEY_ANOTHER_VALUE")
val KEY_NEW_VALUE = intPreferencesKey("KEY_NEW_VALUE")

private const val FILE_OLD_DATA_STORE_PREFERENCES = "old_data_store_preferences"

private const val FILE_NEW_DATA_STORE_PREFERENCES = "new_data_store_preferences"

val Context.oldDataStore by preferencesDataStore(
    name = FILE_OLD_DATA_STORE_PREFERENCES
)

val Context.newDataStore by preferencesDataStore(
    name = FILE_NEW_DATA_STORE_PREFERENCES,
    produceMigrations = {
        listOf(
            object : DataMigration<Preferences> {

                override suspend fun cleanUp() {
                    it.oldDataStore.edit { oldPreferences -> oldPreferences.clear() }
                }

                override suspend fun shouldMigrate(currentData: Preferences): Boolean {
                    val oldValues = it.oldDataStore.data.first()
                    return oldValues.contains(KEY_OLD_VALUE) && oldValues[KEY_OLD_VALUE] is Double
                }

                override suspend fun migrate(currentData: Preferences): Preferences {
                    val oldData = it.oldDataStore.data.first().asMap()
                    val currentMutablePrefs = currentData.toMutablePreferences()
                    mapOldToNewPrefs(oldData, currentMutablePrefs)
                    return currentMutablePrefs.toPreferences()
                }
            }
        )
    }
)

private fun mapOldToNewPrefs(
    oldData: Map<Preferences.Key<*>, Any>,
    currentMutablePrefs: MutablePreferences
) {
    oldData.forEach { (key, value) ->
        when (value) {
            is Double ->
                if (key == KEY_OLD_VALUE) {
                    currentMutablePrefs[KEY_NEW_VALUE] = value.toInt()
                } else {
                    currentMutablePrefs[doublePreferencesKey(key.name)] = value
                }
        }
    }
}
