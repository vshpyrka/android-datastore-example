package com.example.datastore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datastore.databinding.ActivityDataStoreBinding

class DataStoreLauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStoreBinding.inflate(layoutInflater)
        binding.root.applyWindowInsets()
        setContentView(binding.root)

        binding.datastorePreferences.setOnClickListener {
            startActivity(Intent(this, DataStorePreferencesActivity::class.java))
        }
        binding.datastorePreferencesMigration.setOnClickListener {
            startActivity(Intent(this, DataStorePreferencesMigrationActivity::class.java))
        }
        binding.datastoreProtoPreferences.setOnClickListener {
            startActivity(Intent(this, DataStoreProtobufActivity::class.java))
        }
        binding.injectedDataStore.setOnClickListener {
            startActivity(Intent(this, DataStoreInjectedPreferencesActivity::class.java))
        }
        binding.injectedProtoDataStore.setOnClickListener {
            startActivity(Intent(this, DataStoreInjectedProtoPreferencesActivity::class.java))
        }
        binding.jsonDataStore.setOnClickListener {
            startActivity(Intent(this, DataStoreJsonPreferencesActivity::class.java))
        }
        binding.dataStoreDataStoreMigration.setOnClickListener {
            startActivity(Intent(this, DataStoreDataStoreMigrationActivity::class.java))
        }
    }
}
