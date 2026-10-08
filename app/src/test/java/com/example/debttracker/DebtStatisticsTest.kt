package com.example.debttracker

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class DebtStatisticsTest {
    private val today = LocalDate.of(2026, 10, 8)

    private fun debt(
        id: Long,
        type: DebtType,
        amount: Long,
        status: DebtStatus = DebtStatus.ACTIVE,
        returnDate: LocalDate? = null
    ) = Debt(
        id = id,
        personName = "Тест",
        amount = amount,
        type = type,
        returnDate = returnDate,
        comment = null,
        status = status,
        reminderDays = null
    )

    private fun change(id: Long, difference: Long) = DebtHistory(
        debtId = id,
        amountChange = difference,
        balanceAfter = 0,
        changedAt = LocalDateTime.of(2026, 10, 1, 12, 0),
        comment = null
    )

    @Test
    fun totalsAreSeparatedByDebtTypeAndStatus() {
        val debts = listOf(
            debt(1, DebtType.OWED_TO_ME, 700, returnDate = today.minusDays(1)),
            debt(2, DebtType.OWED_TO_ME, 0, DebtStatus.PAID),
            debt(3, DebtType.I_OWE, 400),
            debt(4, DebtType.OWED_TO_ME, 100, returnDate = today)
        )
        val history = listOf(
            change(1, 1000),
            change(1, -300),
            change(2, -250),
            change(3, -50),
            change(99, -10000)
        )

        val result = calculateDebtStatistics(
            debts, history, DebtType.OWED_TO_ME, today
        )

        assertEquals(2, result.activeCount)
        assertEquals(1, result.paidCount)
        assertEquals(800L, result.outstandingAmount)
        assertEquals(1, result.overdueCount)
        assertEquals(550L, result.decreaseAmount)
        assertEquals(2, result.decreaseOperations)

        val other = calculateDebtStatistics(
            debts, history, DebtType.I_OWE, today
        )
        assertEquals(400L, other.outstandingAmount)
        assertEquals(50L, other.decreaseAmount)
    }
}
