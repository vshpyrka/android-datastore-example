package com.example.datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStoreProtobufBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DataStoreProtobufActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStoreProtobufBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(KEY_PROTO_SHARD_PREFERENCES_FILE, Context.MODE_PRIVATE)
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
                val data = protoDataStore.data.first()
                val name = data.userName
                val email = data.userEmail
                binding.userName.setText(name)
                binding.userEmail.setText(email)
                binding.status.text = "Loaded"
            }
        }
        binding.saveDatastore.setOnClickListener {
            lifecycleScope.launch {
                protoDataStore.updateData {
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
