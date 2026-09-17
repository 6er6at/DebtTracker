package com.example.debttracker

import android.content.Context
import androidx.room3.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "debt_tracker.db"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}