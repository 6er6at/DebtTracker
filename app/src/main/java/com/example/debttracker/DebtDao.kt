package com.example.debttracker

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface DebtDao{
    @Query("SELECT * FROM debts")
    suspend fun getAllDebts(): List<Debt>
    @Insert
    suspend fun insertDebt(debt: Debt)
    @Update
    suspend fun updateDebt(debt: Debt)
    @Delete
    suspend fun deleteDebt(debt: Debt)
}