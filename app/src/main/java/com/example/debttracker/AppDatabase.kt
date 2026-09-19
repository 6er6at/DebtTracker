package com.example.debttracker

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase

@ColumnTypeConverters(Converters::class)
@Database(
    entities = [
        Debt::class,
        Payment::class,
        DebtHistory::class
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun debtDao(): DebtDao

    abstract fun debtHistoryDao(): DebtHistoryDao
}