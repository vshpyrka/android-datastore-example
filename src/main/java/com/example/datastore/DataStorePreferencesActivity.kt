package com.example.datastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStorePreferencesBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DataStorePreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStorePreferencesBinding.inflate(layoutInflater)
        binding.root.applyWindowInsets()
        setContentView(binding.root)

        binding.clear.setOnClickListener {
            lifecycleScope.launch {
                dataStore.edit {
                    it[KEY_CONFIG_USER_NAME] = ""
                    it[KEY_CONFIG_USER_LAST_NAME] = ""
                }
                binding.userName.text = null
                binding.userLastName.text = null
                binding.status.text = "Cleared"
            }
        }
        binding.save.setOnClickListener {
            val userName = binding.userName.text.toString()
            val userLastName = binding.userLastName.text.toString()
            lifecycleScope.launch {
                dataStore.edit {
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
                val prefs = dataStore.data.first()
                val name = prefs[KEY_CONFIG_USER_NAME]
                val lastName = prefs[KEY_CONFIG_USER_LAST_NAME]
                binding.userName.setText(name)
                binding.userLastName.setText(lastName)
                binding.status.text = "Loaded"
            }
        }
    }
}
