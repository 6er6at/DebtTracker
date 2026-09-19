package com.example.debttracker

import android.content.Context
import androidx.room3.Room
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object DatabaseProvider {

    private val MIGRATION_1_2 = object : Migration(1, 2) {

        override suspend fun migrate(
            connection: SQLiteConnection
        ) {
            connection.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `debt_history` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `debtId` INTEGER NOT NULL,
                    `amountChange` INTEGER NOT NULL,
                    `balanceAfter` INTEGER NOT NULL,
                    `changedAt` TEXT NOT NULL,
                    `comment` TEXT,
                    FOREIGN KEY(`debtId`)
                        REFERENCES `debts`(`id`)
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE
                )
                """.trimIndent()
            )

            connection.execSQL(
                """
                CREATE INDEX IF NOT EXISTS `index_debt_history_debtId`
                ON `debt_history` (`debtId`)
                """.trimIndent()
            )
        }
    }

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "debt_tracker.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}