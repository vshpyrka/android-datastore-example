package com.example.datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStorePreferencesMigrationBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DataStorePreferencesMigrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStorePreferencesMigrationBinding.inflate(layoutInflater)
        binding.root.applyWindowInsets()
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(KEY_SHARD_PREFERENCES_FILE, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val oldDataStoreFile = preferencesDataStoreFile(KEY_SHARD_PREFERENCES_FILE)
        oldDataStoreFile.delete()
        binding.clear.setOnClickListener {
            binding.status.text = "Cleared"
            binding.userName.text = null
            binding.userLastName.text = null
        }
        binding.save.setOnClickListener {
            val userName = binding.userName.text.toString()
            val userLastName = binding.userLastName.text.toString()
            sharedPreferences.edit().apply {
                putString(PREF_USER_NAME, userName)
                putString(PREF_USER_LAST_NAME, userLastName)
                apply()
                binding.status.text = "Saved"
            }
        }
        binding.load.setOnClickListener {
            lifecycleScope.launch {
                val data = migratedDataStore.data.first()
                val name = data[KEY_CONFIG_USER_NAME]
                val lastName = data[KEY_CONFIG_USER_LAST_NAME]
                binding.userName.setText(name)
                binding.userLastName.setText(lastName)
                binding.status.text = "Loaded"
            }
        }
        binding.saveDatastore.setOnClickListener {
            val userName = binding.userName.text.toString()
            val userLastName = binding.userLastName.text.toString()
            lifecycleScope.launch {
                migratedDataStore.edit {
                    it[KEY_CONFIG_USER_NAME] = userName
                    it[KEY_CONFIG_USER_LAST_NAME] = userLastName
                }
                binding.status.text = "Saved"
            }
        }
    }
}
