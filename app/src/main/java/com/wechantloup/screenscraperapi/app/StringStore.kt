package com.wechantloup.screenscraperapi.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val DATA_STORE_NAME = "StringRepository"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
class StringStore(context: Context) {

    private val dataStore = context.applicationContext.dataStore

    suspend fun save(id: String, value: String) = withContext(Dispatchers.IO) {
        val key = stringPreferencesKey(id)
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun get(id: String): String? = withContext(Dispatchers.IO) {
        val key = stringPreferencesKey(id)
        val flowValue: Flow<String?> = dataStore.data
            .map { preferences -> preferences[key] }
        flowValue.first()
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        val key = stringPreferencesKey(id)
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}
