package com.example.debttracker

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface DebtHistoryDao {

    @Insert
    suspend fun insertHistory(history: DebtHistory)

    @Query(
        "SELECT * FROM debt_history " +
                "WHERE debtId = :debtId " +
                "ORDER BY changedAt DESC"
    )
    suspend fun getHistoryForDebt(
        debtId: Long
    ): List<DebtHistory>
}