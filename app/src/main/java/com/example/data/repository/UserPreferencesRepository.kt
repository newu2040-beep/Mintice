package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mintice_preferences")

data class UserPreferences(
    val themeName: String = "BLOSSOM",
    val isCompactMode: Boolean = false,
    val is24HourFormat: Boolean = false,
    val userName: String = "",
    val startOfWeekMonday: Boolean = true,
    val isDarkMode: Boolean = false,
    val themeMode: String = "SYSTEM", // "LIGHT", "DARK", "SYSTEM"
    val minimalDecorations: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {
    companion object {
        val KEY_THEME = stringPreferencesKey("pref_theme")
        val KEY_COMPACT = booleanPreferencesKey("pref_compact_mode")
        val KEY_24_HOUR = booleanPreferencesKey("pref_24_hour_format")
        val KEY_USER_NAME = stringPreferencesKey("pref_user_name")
        val KEY_START_OF_WEEK = booleanPreferencesKey("pref_start_of_week_monday")
        val KEY_DARK_MODE = booleanPreferencesKey("pref_dark_mode")
        val KEY_THEME_MODE = stringPreferencesKey("pref_theme_mode")
        val KEY_MINIMAL_DECORS = booleanPreferencesKey("pref_minimal_decorations")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val explicitDarkMode = prefs[KEY_DARK_MODE] ?: false
        val savedThemeMode = prefs[KEY_THEME_MODE] ?: if (explicitDarkMode) "DARK" else "SYSTEM"
        UserPreferences(
            themeName = prefs[KEY_THEME] ?: "BLOSSOM",
            isCompactMode = prefs[KEY_COMPACT] ?: false,
            is24HourFormat = prefs[KEY_24_HOUR] ?: false,
            userName = prefs[KEY_USER_NAME] ?: "",
            startOfWeekMonday = prefs[KEY_START_OF_WEEK] ?: true,
            isDarkMode = explicitDarkMode,
            themeMode = savedThemeMode,
            minimalDecorations = prefs[KEY_MINIMAL_DECORS] ?: false
        )
    }

    suspend fun setTheme(themeName: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = themeName
        }
    }

    suspend fun setCompactMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_COMPACT] = enabled
        }
    }

    suspend fun set24HourFormat(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_24_HOUR] = enabled
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
            prefs[KEY_THEME_MODE] = if (enabled) "DARK" else "LIGHT"
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode
            prefs[KEY_DARK_MODE] = (mode == "DARK")
        }
    }

    suspend fun setMinimalDecorations(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MINIMAL_DECORS] = enabled
        }
    }
}
