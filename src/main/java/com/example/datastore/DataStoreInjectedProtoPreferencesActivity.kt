package com.example.datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStoreInjectedProtoPreferencesBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class DataStoreInjectedProtoPreferencesActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStore<Credentials>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStoreInjectedProtoPreferencesBinding.inflate(layoutInflater)
        binding.root.applyWindowInsets()
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(KEY_INJECTED_PROTO_SHARD_PREFERENCES_FILE, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        binding.clear.setOnClickListener {
            binding.status.text = "Cleared"
            binding.userName.text = null
            binding.userEmail.text = null
        }
        binding.save.setOnClickListener {
            val userName = binding.userName.text.toString()
            val userLastName = binding.userEmail.text.toString()
            sharedPreferences.edit().apply {
                putString(PREF_USER_NAME, userName)
                putString(PREF_USER_LAST_NAME, userLastName)
                apply()
                binding.status.text = "Saved"
            }
        }
        binding.loadOldPrefs.setOnClickListener {
            binding.userName.text = null
            binding.userEmail.text = null
            val userName = sharedPreferences.getString(PREF_USER_NAME, null)
            val userLastName = sharedPreferences.getString(PREF_USER_LAST_NAME, null)
            binding.userName.setText(userName)
            binding.userEmail.setText(userLastName)
            binding.status.text = "Loaded"
        }
        binding.load.setOnClickListener {
            lifecycleScope.launch {
                val data = dataStore.data.first()
                val name = data.userName
                val email = data.userEmail
                binding.userName.setText(name)
                binding.userEmail.setText(email)
                binding.status.text = "Loaded"
            }
        }
        binding.saveDatastore.setOnClickListener {
            lifecycleScope.launch {
                dataStore.updateData {
                    val userName = binding.userName.text.toString()
                    val userLastName = binding.userEmail.text.toString()
                    it.copy {
                        this.userName = userName
                        this.userEmail = userLastName
                        userType = Credentials.UserType.REGULAR
                    }
                    // or
                    credentials {
                        this.userName = userName
                        this.userEmail = userLastName
                        userType = Credentials.UserType.REGULAR
                    }
                }
                binding.status.text = "Saved"
            }
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
class DataStoreInjectedProtoPreferenceModule {

    @Singleton
    @Provides
    fun provideDataStoreInjectedProtoPreference(@ApplicationContext context: Context): DataStore<Credentials> {
        return DataStoreFactory.create(
            serializer = CredentialsSerializer,
            produceFile = { context.dataStoreFile(KEY_INJECTED_PROTO_SHARD_PREFERENCES_FILE) },
            migrations = listOf(
                SharedPreferencesMigration(
                    context,
                    KEY_INJECTED_PROTO_SHARD_PREFERENCES_FILE
                ) { sharedPrefs: SharedPreferencesView, credentials: Credentials ->
                    val type = if (sharedPrefs.getBoolean("KEY_ADMIN", false)) {
                        Credentials.UserType.ADMIN
                    } else {
                        Credentials.UserType.REGULAR
                    }
                    credentials.copy {
                        userName = sharedPrefs.getString(PREF_USER_NAME)
                            ?: throw IOException("Failed to migrate")
                        userEmail = sharedPrefs.getString(PREF_USER_LAST_NAME) ?: throw IOException(
                            "Failed to migrate"
                        )
                        userType = type
                    }
                }
            )
        )
    }
}
