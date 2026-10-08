package com.example.debttracker

import java.time.LocalDate

/**
 * Статистика рассчитывается только по существующим долгам.
 * Удалённые долги и их история в подсчёт не входят.
 */
data class DebtStatistics(
    val activeCount: Int,
    val paidCount: Int,
    val outstandingAmount: Long,
    val overdueCount: Int,
    val decreaseAmount: Long,
    val decreaseOperations: Int
)

fun calculateDebtStatistics(
    debts: List<Debt>,
    history: List<DebtHistory>,
    type: DebtType,
    today: LocalDate = LocalDate.now()
): DebtStatistics {
    val matchingDebts = debts.filter { it.type == type }
    val activeDebts = matchingDebts.filter {
        it.status == DebtStatus.ACTIVE
    }
    val ids = matchingDebts.mapTo(HashSet()) { it.id }

    // Уменьшение суммы долга может быть возвратом
    // или ручной корректировкой. Не называем его оплатой.
    val decreases = history.filter {
        it.debtId in ids && it.amountChange < 0L
    }

    return DebtStatistics(
        activeCount = activeDebts.size,
        paidCount = matchingDebts.count { it.status == DebtStatus.PAID },
        outstandingAmount = activeDebts.sumOf { it.amount },
        overdueCount = activeDebts.count {
            it.returnDate?.isBefore(today) == true
        },
        decreaseAmount = decreases.sumOf { -it.amountChange },
        decreaseOperations = decreases.size
    )
}
