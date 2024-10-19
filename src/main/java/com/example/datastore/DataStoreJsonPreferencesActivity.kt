package com.example.datastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityDataStoreJsonPreferencesBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream

val Context.jsonDataStore: DataStore<UserPreferences> by dataStore(
    serializer = UserPreferencesSerializer,
    fileName = "app_json_data_store_preferences"
)

class DataStoreJsonPreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDataStoreJsonPreferencesBinding.inflate(layoutInflater)
        binding.root.applyWindowInsets()
        setContentView(binding.root)

        binding.clear.setOnClickListener {
            lifecycleScope.launch {
                jsonDataStore.updateData {
                    it.copy(
                        userName = "",
                        userEmail = ""
                    )
                }
                binding.userName.text = null
                binding.userEmail.text = null
                binding.status.text = "Cleared"
            }
        }
        binding.save.setOnClickListener {
            val userName = binding.userName.text.toString()
            val userLastName = binding.userEmail.text.toString()
            lifecycleScope.launch {
                jsonDataStore.updateData {
                    it.copy(
                        userName = userName,
                        userEmail = userLastName
                    )
                }
                binding.userName.text = null
                binding.userEmail.text = null
                binding.status.text = "Saved"
            }
        }
        binding.load.setOnClickListener {
            lifecycleScope.launch {
                val prefs = jsonDataStore.data.first()
                val name = prefs.userName
                val lastName = prefs.userEmail
                binding.userName.setText(name)
                binding.userEmail.setText(lastName)
                binding.status.text = "Loaded"
            }
        }
    }
}

enum class UserRole {
    UNKNOWN,
    ADMIN,
    MANAGER,
    REGULAR
}

@Serializable
data class UserPreferences(
    val userName: String,
    val userEmail: String,
    val userRole: UserRole,
)

object UserPreferencesSerializer : Serializer<UserPreferences> {

    override val defaultValue = UserPreferences("", "", UserRole.UNKNOWN)

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return Json.decodeFromString(
                UserPreferences.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Failed to parse preferences", e)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(
                UserPreferences.serializer(),
                t
            ).encodeToByteArray()
        )
    }
}
