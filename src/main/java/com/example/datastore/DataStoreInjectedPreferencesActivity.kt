package com.example.datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStoreInjectedPreferencesBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class DataStoreInjectedPreferencesActivity : AppCompatActivity() {

    @Inject
    lateinit var injectedDataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStoreInjectedPreferencesBinding.inflate(layoutInflater)
        binding.root.applyWindowInsets()
        setContentView(binding.root)

        binding.clear.setOnClickListener {
            lifecycleScope.launch {
                injectedDataStore.edit {
                    it[KEY_CONFIG_USER_NAME] = ""
                    it[KEY_CONFIG_USER_LAST_NAME] = ""
                }
                binding.status.text = "Cleared"
            }
        }
        binding.save.setOnClickListener {
            val userName = binding.userName.text.toString()
            val userLastName = binding.userLastName.text.toString()
            lifecycleScope.launch {
                injectedDataStore.edit {
                    it[KEY_CONFIG_USER_NAME] = userName
                    it[KEY_CONFIG_USER_LAST_NAME] = userLastName
                }
                binding.userName.text = null
                binding.userLastName.text = null
                binding.status.text = "Saved"
            }
        }
        binding.load.setOnClickListener {
            lifecycleScope.launch {
                val prefs = injectedDataStore.data.first()
                val name = prefs[KEY_CONFIG_USER_NAME]
                val lastName = prefs[KEY_CONFIG_USER_LAST_NAME]
                binding.userName.setText(name)
                binding.userLastName.setText(lastName)
                binding.status.text = "Loaded"
            }
        }
    }
}

const val USER_SHARED_PREFERENCES = "app_injected_shared_preferences"

@InstallIn(SingletonComponent::class)
@Module
class DataStoreInjectedPreferencesModule {

    @Singleton
    @Provides
    fun provideDataStoreInjectedPreferences(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = {
                    emptyPreferences()
                }
            ),
            produceFile = { appContext.preferencesDataStoreFile(USER_SHARED_PREFERENCES) }
        )
    }
}
