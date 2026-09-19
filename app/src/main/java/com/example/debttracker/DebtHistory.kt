package com.example.debttracker

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "debt_history",
    foreignKeys = [
        ForeignKey(
            entity = Debt::class,
            parentColumns = ["id"],
            childColumns = ["debtId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["debtId"])
    ]
)
data class DebtHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val debtId: Long,

    // На сколько изменился долг:
    // +2000 = долг увеличился
    // -500 = долг уменьшился
    val amountChange: Long,

    // Остаток после изменения
    val balanceAfter: Long,

    val changedAt: LocalDateTime,

    val comment: String?
)

