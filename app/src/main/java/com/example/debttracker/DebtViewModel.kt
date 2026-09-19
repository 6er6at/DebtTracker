package com.example.debttracker

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room3.withWriteTransaction

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import java.time.LocalDateTime


class DebtViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _debts =
        MutableStateFlow<List<Debt>>(emptyList())

    val debts: StateFlow<List<Debt>> =
        _debts

    private val _history =
        MutableStateFlow<List<DebtHistory>>(emptyList())

    val history: StateFlow<List<DebtHistory>> =
        _history

    private val database =
        DatabaseProvider.getDatabase(
            application
        )

    private val debtDao =
        database.debtDao()

    private val historyDao =
        database.debtHistoryDao()


    init {

        viewModelScope.launch {

            _debts.value =
                debtDao.getAllDebts()

            // Восстанавливаем все будущие
            // напоминания при запуске приложения.
            ReminderScheduler.rescheduleAll(
                getApplication<Application>()
            )
        }
    }


    fun addDebt(
        debt: Debt
    ) {

        viewModelScope.launch {

            database.withWriteTransaction {

                debtDao.insertDebt(
                    debt
                )

                historyDao.insertHistory(
                    DebtHistory(
                        debtId =
                            debt.id,

                        amountChange =
                            debt.amount,

                        balanceAfter =
                            debt.amount,

                        changedAt =
                            LocalDateTime.now(),

                        comment =
                            "Создание долга"
                    )
                )
            }

            // ВАЖНО:
            // здесь реально создаётся AlarmManager alarm.
            ReminderScheduler.schedule(
                getApplication<Application>(),
                debt
            )

            _debts.value =
                debtDao.getAllDebts()
        }
    }


    fun updateDebt(
        debt: Debt
    ) {

        viewModelScope.launch {

            database.withWriteTransaction {

                val oldDebt =
                    debtDao.getDebtById(
                        debt.id
                    )

                if (oldDebt != null) {

                    debtDao.updateDebt(
                        debt
                    )

                    val amountChange =
                        debt.amount -
                                oldDebt.amount

                    if (
                        amountChange != 0L
                    ) {

                        historyDao.insertHistory(
                            DebtHistory(
                                debtId =
                                    debt.id,

                                amountChange =
                                    amountChange,

                                balanceAfter =
                                    debt.amount,

                                changedAt =
                                    LocalDateTime.now(),

                                comment =
                                    "Изменение суммы"
                            )
                        )
                    }
                }
            }

            // Старый alarm сначала отменяется,
            // затем ставится новый с новой датой
            // или новым количеством дней.
            ReminderScheduler.schedule(
                getApplication<Application>(),
                debt
            )

            _debts.value =
                debtDao.getAllDebts()
        }
    }


    fun changeDebtAmount(
        debtId: Long,
        amountChange: Long,
        comment: String?
    ) {

        viewModelScope.launch {

            database.withWriteTransaction {

                val currentDebt =
                    debtDao.getDebtById(
                        debtId
                    )

                if (currentDebt != null) {

                    val newAmount =
                        currentDebt.amount +
                                amountChange

                    if (newAmount >= 0L) {

                        val newStatus =
                            if (newAmount == 0L) {
                                DebtStatus.PAID
                            } else {
                                DebtStatus.ACTIVE
                            }

                        val updatedDebt =
                            currentDebt.copy(
                                amount =
                                    newAmount,

                                status =
                                    newStatus
                            )

                        debtDao.updateDebt(
                            updatedDebt
                        )

                        historyDao.insertHistory(
                            DebtHistory(
                                debtId =
                                    debtId,

                                amountChange =
                                    amountChange,

                                balanceAfter =
                                    newAmount,

                                changedAt =
                                    LocalDateTime.now(),

                                comment =
                                    comment
                            )
                        )
                    }
                }
            }

            val updatedDebt =
                debtDao.getDebtById(
                    debtId
                )

            if (updatedDebt != null) {

                if (
                    updatedDebt.status ==
                    DebtStatus.PAID
                ) {

                    ReminderScheduler.cancel(
                        getApplication<Application>(),
                        updatedDebt.id
                    )

                } else {

                    ReminderScheduler.schedule(
                        getApplication<Application>(),
                        updatedDebt
                    )
                }
            }

            _debts.value =
                debtDao.getAllDebts()

            _history.value =
                historyDao.getHistoryForDebt(
                    debtId
                )
        }
    }


    fun deleteDebt(
        debt: Debt
    ) {

        viewModelScope.launch {

            ReminderScheduler.cancel(
                getApplication<Application>(),
                debt.id
            )

            debtDao.deleteDebt(
                debt
            )

            _debts.value =
                debtDao.getAllDebts()

            _history.value =
                emptyList()
        }
    }


    fun loadHistory(
        debtId: Long
    ) {

        viewModelScope.launch {

            _history.value =
                historyDao.getHistoryForDebt(
                    debtId
                )
        }
    }


    fun clearHistory() {

        _history.value =
            emptyList()
    }
}