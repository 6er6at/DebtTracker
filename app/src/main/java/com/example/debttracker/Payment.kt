package com.example.debttracker
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey
    val id: Long,
    val debtId: Long,
    val amount: Long,
    val paymentDate: LocalDate,
    val comment: String?,
)