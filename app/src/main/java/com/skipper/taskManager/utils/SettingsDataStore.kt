package com.skipper.taskManager.utils

// SettingsDataStore.kt
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingsDataStore {
    // Extension property to create/access the DataStore instance
    private val Context.dataStore by preferencesDataStore("settings")

    // Key for storing the primary color
    val PRIMARY_COLOR = intPreferencesKey("primary_color")

    // Save selected color as an ARGB integer
    suspend fun savePrimaryColor(context: Context, color: Int) {
        context.dataStore.edit { preferences ->
            preferences[PRIMARY_COLOR] = color
        }
    }

    // Flow to observe changes to the primary color
    val Context.primaryColorFlow: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[PRIMARY_COLOR]
        }
}