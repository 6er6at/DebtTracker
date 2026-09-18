package com.example.debttracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DebtViewModel(application: Application) : AndroidViewModel(application) {

    private val _debts = MutableStateFlow<List<Debt>>(emptyList())

    val debts: StateFlow<List<Debt>> = _debts

    private val database = DatabaseProvider.getDatabase(
        application
    )

    private val debtDao = database.debtDao()

    init {
        viewModelScope.launch {
            _debts.value = debtDao.getAllDebts()
        }
    }

    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            debtDao.insertDebt(debt)
            _debts.value = debtDao.getAllDebts()
        }
    }

    fun updateDebt(debt: Debt) {
        viewModelScope.launch {
            debtDao.updateDebt(debt)
            _debts.value = debtDao.getAllDebts()
        }
    }

    fun deleteDebt(debt: Debt) {
        viewModelScope.launch {
            debtDao.deleteDebt(debt)
            _debts.value = debtDao.getAllDebts()
        }
    }
}