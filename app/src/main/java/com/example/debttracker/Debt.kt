package com.example.debttracker
import androidx.room3.Entity
import java.time.LocalDate
import androidx.room3.PrimaryKey

enum class DebtType {
    OWED_TO_ME,
    I_OWE
}
enum class DebtStatus {
    ACTIVE,
    PAID
}
enum class ReminderDays(val days: Int) {
    ONE(1),
    THREE(3),
    FIVE(5),
    SEVEN(7),
    TEN(10)
}
@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey
    val id: Long,
    val personName: String,
    val amount: Long,
    val type: DebtType,
    val returnDate: LocalDate?,
    val comment: String?,
    val status: DebtStatus,
    val reminderDays: ReminderDays?,
)