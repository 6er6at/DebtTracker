package com.example.debttracker

import android.content.SharedPreferences


enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}


enum class SortMode {
    NONE,
    AMOUNT_DESC,
    AMOUNT_ASC,
    DATE_ASC,
    DATE_DESC,
    NAME_ASC
}


object AppSettings {

    private const val THEME_KEY = "theme_mode"
    private const val SORT_KEY = "sort_mode"


    fun loadTheme(
        preferences: SharedPreferences
    ): ThemeMode {

        return try {

            ThemeMode.valueOf(
                preferences.getString(
                    THEME_KEY,
                    ThemeMode.SYSTEM.name
                ) ?: ThemeMode.SYSTEM.name
            )

        } catch (_: Exception) {

            ThemeMode.SYSTEM
        }
    }


    fun saveTheme(
        preferences: SharedPreferences,
        mode: ThemeMode
    ) {

        preferences.edit()
            .putString(
                THEME_KEY,
                mode.name
            )
            .apply()
    }


    fun loadSort(
        preferences: SharedPreferences
    ): SortMode {

        return try {

            SortMode.valueOf(
                preferences.getString(
                    SORT_KEY,
                    SortMode.NONE.name
                ) ?: SortMode.NONE.name
            )

        } catch (_: Exception) {

            SortMode.NONE
        }
    }


    fun saveSort(
        preferences: SharedPreferences,
        mode: SortMode
    ) {

        preferences.edit()
            .putString(
                SORT_KEY,
                mode.name
            )
            .apply()
    }
}