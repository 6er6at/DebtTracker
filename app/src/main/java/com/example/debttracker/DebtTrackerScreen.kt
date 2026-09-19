package com.example.debttracker

import android.app.Application

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.debttracker.ui.theme.DebtTrackerTheme


@Composable
fun DebtTrackerRoot() {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "debt_tracker_settings",
            0
        )
    }

    var themeMode by remember {
        mutableStateOf(
            AppSettings.loadTheme(preferences)
        )
    }

    var sortMode by remember {
        mutableStateOf(
            AppSettings.loadSort(preferences)
        )
    }

    val darkTheme = when (themeMode) {

        ThemeMode.SYSTEM ->
            isSystemInDarkTheme()

        ThemeMode.LIGHT ->
            false

        ThemeMode.DARK ->
            true
    }

    DebtTrackerTheme(
        darkTheme = darkTheme
    ) {

        DebtTrackerScreen(
            sortMode = sortMode,
            onSortChanged = { mode ->
                sortMode = mode

                AppSettings.saveSort(
                    preferences,
                    mode
                )
            },

            themeMode = themeMode,
            onThemeChanged = { mode ->
                themeMode = mode

                AppSettings.saveTheme(
                    preferences,
                    mode
                )
            }
        )
    }
}


@Composable
fun DebtTrackerScreen(
    sortMode: SortMode,
    onSortChanged: (SortMode) -> Unit,

    themeMode: ThemeMode,
    onThemeChanged: (ThemeMode) -> Unit
) {

    var selectedTab by remember {
        mutableStateOf(0)
    }

    var showAddDebt by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    var selectedDebtId by remember {
        mutableStateOf<Long?>(null)
    }

    var showEditDebt by remember {
        mutableStateOf(false)
    }

    var showAmountChange by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val debtViewModel: DebtViewModel = viewModel(
        factory = AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    val debts by debtViewModel.debts.collectAsState()

    val history by debtViewModel.history.collectAsState()

    val selectedDebt = debts.firstOrNull {
        it.id == selectedDebtId
    }

    var filteredDebts = debts.filter { debt ->

        if (selectedTab == 0) {
            debt.type == DebtType.OWED_TO_ME
        } else {
            debt.type == DebtType.I_OWE
        }
    }

    val search = searchQuery.trim()

    if (search.isNotBlank()) {

        filteredDebts = filteredDebts.filter { debt ->

            debt.personName.contains(
                search,
                ignoreCase = true
            ) ||

                    debt.comment?.contains(
                        search,
                        ignoreCase = true
                    ) == true
        }
    }

    filteredDebts = when (sortMode) {

        SortMode.NONE ->
            filteredDebts

        SortMode.AMOUNT_DESC ->
            filteredDebts.sortedByDescending {
                it.amount
            }

        SortMode.AMOUNT_ASC ->
            filteredDebts.sortedBy {
                it.amount
            }

        SortMode.DATE_ASC ->
            filteredDebts.sortedWith(
                compareBy<Debt> {
                    it.returnDate == null
                }.thenBy {
                    it.returnDate
                }
            )

        SortMode.DATE_DESC ->
            filteredDebts.sortedWith(
                compareBy<Debt> {
                    it.returnDate == null
                }.thenByDescending {
                    it.returnDate
                }
            )

        SortMode.NAME_ASC ->
            filteredDebts.sortedBy {
                it.personName.lowercase()
            }
    }

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    showAddDebt = true
                }
            ) {

                Text(
                    text = "+",
                    fontSize = 28.sp
                )
            }
        },

        bottomBar = {

            DebtBottomBar(
                searchQuery = searchQuery,

                onSearchChanged = {
                    searchQuery = it
                },

                onSettingsClick = {
                    showSettings = true
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = 16.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Учёт долгов",
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Контроль ваших долгов",
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTab = 0
                        },
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Мне должны",
                        fontSize = 15.sp
                    )

                    if (selectedTab == 0) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTab = 1
                        },
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Я должен",
                        fontSize = 15.sp
                    )

                    if (selectedTab == 1) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                )
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Долгов: ${filteredDebts.size}",
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            DebtList(
                debts = filteredDebts,

                onDebtClick = { debt ->

                    selectedDebtId = debt.id

                    debtViewModel.clearHistory()

                    debtViewModel.loadHistory(
                        debt.id
                    )
                },

                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showAddDebt) {

        AddDebtDialog(
            debtType = if (selectedTab == 0) {
                DebtType.OWED_TO_ME
            } else {
                DebtType.I_OWE
            },

            viewModel = debtViewModel,

            onDismiss = {
                showAddDebt = false
            }
        )
    }

    if (
        selectedDebt != null &&
        !showEditDebt &&
        !showAmountChange
    ) {

        DebtDetailsSheet(
            debt = selectedDebt,
            history = history,

            onEdit = {
                showEditDebt = true
            },

            onAmountChange = {
                showAmountChange = true
            },

            onDelete = {

                debtViewModel.deleteDebt(
                    selectedDebt
                )

                selectedDebtId = null
            },

            onDismiss = {

                selectedDebtId = null

                debtViewModel.clearHistory()
            }
        )
    }

    if (
        selectedDebt != null &&
        showEditDebt
    ) {

        EditDebtDialog(
            debt = selectedDebt,

            viewModel = debtViewModel,

            onDismiss = {
                showEditDebt = false
            }
        )
    }

    if (
        selectedDebt != null &&
        showAmountChange
    ) {

        AmountChangeDialog(
            debt = selectedDebt,

            onDismiss = {
                showAmountChange = false
            },

            onSave = { change, comment ->

                debtViewModel.changeDebtAmount(
                    debtId = selectedDebt.id,
                    amountChange = change,
                    comment = comment
                )

                showAmountChange = false
            }
        )
    }

    if (showSettings) {

        SettingsSheet(
            themeMode = themeMode,
            sortMode = sortMode,

            onThemeChanged = onThemeChanged,
            onSortChanged = onSortChanged,

            onDismiss = {
                showSettings = false
            }
        )
    }
}