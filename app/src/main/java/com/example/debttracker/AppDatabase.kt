package com.example.debttracker

import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.ColumnTypeConverters

@ColumnTypeConverters(Converters::class)
@Database(
    entities = [Debt::class, Payment::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun debtDao(): DebtDao
}