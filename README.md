# android-datastore-example

Set of Jetpack DataStore APIs examples

https://github.com/vshpyrka/android-datastore-example/assets/2741602/f6bfed52-f266-4d84-9b7f-ee43112d3ee1

* [DataStorePreferencesActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStorePreferencesActivity.kt) - Shows a simple example of how to use `DataStore` library to save and retrieve informtion from DataStore preferences
Example:
```
// Get DataStore instance
  val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
      name = "datastore_prefs"
  )

// Read:
  lifecycleScope.launch {
  val prefs = dataStore.data.first()
  val name = prefs[KEY_CONFIG_USER_NAME]
  val lastName = prefs[KEY_CONFIG_USER_LAST_NAME]

// Write:
  lifecycleScope.launch {
    dataStore.edit {
        it[KEY_CONFIG_USER_NAME] = ""
        it[KEY_CONFIG_USER_LAST_NAME] = ""
    }
```
* [DataStorePreferencesMigrationActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStorePreferencesMigrationActivity.kt) - Shows example of how to migrate data from SharedPreferences to DataStore using `preferencesDataStore(produceMigrations = { listOf(SharedPreferencesMigration(context, "prefs_file")) })` implementation
* [DataStoreProtobufActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStoreProtobufActivity.kt) - Shows example of how to use Protocol Buffers(Protobuf) format to store data in DataStore. Protobuf object schema is stored in the separate directory - [proto](https://github.com/vshpyrka/android-datastore-example/tree/main/src/main/proto/com/example/datastore). In addition [build.gradle](https://github.com/vshpyrka/android-datastore-example/blob/main/build.gradle.kts#L87) uses Kotlin Protobuf-lite which provides additional builder methods for Protobuf objects creation
* [DataStoreDataStoreMigrationActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStoreDataStoreMigrationActivity.kt) - Shows example of how to create and provide DataStore instance using Hilt Dependency Injection
Example:
```
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
```
* [DataStoreInjectedProtoPreferencesActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStoreInjectedProtoPreferencesActivity.kt) - Shows example of how to create and provide DataStore instance with Protobuf serialization implementation using `DataStoreFactory.create` method.
Example:
```
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
            ...
```
* [DataStoreJsonPreferencesActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStoreJsonPreferencesActivity.kt) - Shows example of how to provide custom DataStore Serializer implementation, for instance using `kotlinx.serialization`
* [DataStoreDataStoreMigrationActivity.kt](https://github.com/vshpyrka/android-datastore-example/blob/main/src/main/java/com/example/datastore/DataStoreDataStoreMigrationActivity.kt) - Shows example of how to make data migration from one version of DataStore to another using `DataMigration<Preferences>` implementation
