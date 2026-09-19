package com.example.debttracker

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.content.ContextCompat

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.debttracker.ui.theme.DebtTrackerTheme

import kotlinx.coroutines.launch


@Composable
fun DebtTrackerRoot() {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "debt_tracker_settings",
            0
        )
    }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) {
            AppSettings.markNotificationPermissionAsked(
                preferences
            )
        }

    LaunchedEffect(Unit) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            val permissionGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

            val alreadyAsked =
                AppSettings.wasNotificationPermissionAsked(
                    preferences
                )

            if (
                !permissionGranted &&
                !alreadyAsked
            ) {

                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    var themeMode by remember {
        mutableStateOf(
            AppSettings.loadTheme(
                preferences
            )
        )
    }

    var sortMode by remember {
        mutableStateOf(
            AppSettings.loadSort(
                preferences
            )
        )
    }

    val darkTheme =
        when (themeMode) {

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

            onSortChanged = {
                sortMode = it

                AppSettings.saveSort(
                    preferences,
                    it
                )
            },

            themeMode = themeMode,

            onThemeChanged = {
                themeMode = it

                AppSettings.saveTheme(
                    preferences,
                    it
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

    val pagerState =
        rememberPagerState(
            pageCount = {
                2
            }
        )

    val coroutineScope =
        rememberCoroutineScope()

    var showAddDebt by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    var showArchive by remember {
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

    val debtViewModel: DebtViewModel =
        viewModel(
            factory =
                AndroidViewModelFactory
                    .getInstance(
                        context.applicationContext
                                as Application
                    )
        )

    val debts by
    debtViewModel.debts.collectAsState()

    val history by
    debtViewModel.history.collectAsState()

    val selectedDebt =
        debts.firstOrNull {
            it.id == selectedDebtId
        }

    fun getFilteredDebts(
        tab: Int
    ): List<Debt> {

        var result =
            debts.filter { debt ->

                debt.status == DebtStatus.ACTIVE &&

                        if (tab == 0) {
                            debt.type ==
                                    DebtType.OWED_TO_ME
                        } else {
                            debt.type ==
                                    DebtType.I_OWE
                        }
            }

        val search =
            searchQuery.trim()

        if (search.isNotBlank()) {

            result =
                result.filter { debt ->

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

        return when (sortMode) {

            SortMode.NONE ->
                result

            SortMode.AMOUNT_DESC ->
                result.sortedByDescending {
                    it.amount
                }

            SortMode.AMOUNT_ASC ->
                result.sortedBy {
                    it.amount
                }

            SortMode.DATE_ASC ->
                result.sortedWith(
                    compareBy<Debt> {
                        it.returnDate == null
                    }.thenBy {
                        it.returnDate
                    }
                )

            SortMode.DATE_DESC ->
                result.sortedWith(
                    compareBy<Debt> {
                        it.returnDate == null
                    }.thenByDescending {
                        it.returnDate
                    }
                )

            SortMode.NAME_ASC ->
                result.sortedBy {
                    it.personName.lowercase()
                }
        }
    }

    val currentTab =
        pagerState.currentPage

    val currentDebts =
        getFilteredDebts(
            currentTab
        )

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
                modifier =
                    Modifier.height(16.dp)
            )

            Text(
                text = "Учёт долгов",

                style =
                    MaterialTheme.typography
                        .headlineLarge
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Все ваши обязательства в одном месте",

                style =
                    MaterialTheme.typography
                        .bodyLarge,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),

                color =
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant,

                shape =
                    MaterialTheme
                        .shapes
                        .large
            ) {

                Row(
                    modifier =
                        Modifier.padding(4.dp)
                ) {

                    DebtTab(
                        text = "Мне должны",

                        selected =
                            currentTab == 0,

                        modifier =
                            Modifier.weight(1f),

                        onClick = {

                            coroutineScope.launch {

                                pagerState.animateScrollToPage(
                                    0
                                )
                            }
                        }
                    )

                    DebtTab(
                        text = "Я должен",

                        selected =
                            currentTab == 1,

                        modifier =
                            Modifier.weight(1f),

                        onClick = {

                            coroutineScope.launch {

                                pagerState.animateScrollToPage(
                                    1
                                )
                            }
                        }
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "Долгов: ${currentDebts.size}",

                    style =
                        MaterialTheme.typography
                            .titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text =
                        when (sortMode) {

                            SortMode.NONE ->
                                "Без сортировки"

                            SortMode.AMOUNT_DESC ->
                                "По сумме ↓"

                            SortMode.AMOUNT_ASC ->
                                "По сумме ↑"

                            SortMode.DATE_ASC ->
                                "По дате ↑"

                            SortMode.DATE_DESC ->
                                "По дате ↓"

                            SortMode.NAME_ASC ->
                                "По имени"
                        },

                    style =
                        MaterialTheme.typography
                            .bodySmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            HorizontalPager(
                state = pagerState,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
            ) { page ->

                val pageDebts =
                    getFilteredDebts(page)

                DebtList(
                    debts = pageDebts,

                    onDebtClick = { debt ->

                        selectedDebtId =
                            debt.id

                        debtViewModel.clearHistory()

                        debtViewModel.loadHistory(
                            debt.id
                        )
                    },

                    modifier =
                        Modifier.fillMaxSize()
                )
            }
        }
    }


    if (showAddDebt) {

        AddDebtDialog(

            debtType =
                if (currentTab == 0) {
                    DebtType.OWED_TO_ME
                } else {
                    DebtType.I_OWE
                },

            viewModel =
                debtViewModel,

            onDismiss = {
                showAddDebt = false
            }
        )
    }


    if (showSettings) {

        SettingsSheet(

            themeMode =
                themeMode,

            sortMode =
                sortMode,

            onThemeChanged =
                onThemeChanged,

            onSortChanged =
                onSortChanged,

            onArchiveClick = {

                showSettings = false

                showArchive = true
            },

            onDismiss = {
                showSettings = false
            }
        )
    }


    if (showArchive) {

        ArchivedDebtsSheet(

            debts =
                debts.filter {
                    it.status == DebtStatus.PAID
                },

            onDebtClick = { debt ->

                showArchive = false

                selectedDebtId =
                    debt.id

                debtViewModel.clearHistory()

                debtViewModel.loadHistory(
                    debt.id
                )
            },

            onDismiss = {
                showArchive = false
            }
        )
    }


    if (
        selectedDebt != null &&
        !showEditDebt &&
        !showAmountChange
    ) {

        DebtDetailsSheet(

            debt =
                selectedDebt,

            history =
                history,

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

            debt =
                selectedDebt,

            viewModel =
                debtViewModel,

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

            debt =
                selectedDebt,

            onDismiss = {
                showAmountChange = false
            },

            onSave = {
                    change,
                    comment ->

                debtViewModel.changeDebtAmount(

                    debtId =
                        selectedDebt.id,

                    amountChange =
                        change,

                    comment =
                        comment
                )

                showAmountChange = false
            }
        )
    }
}


@Composable
private fun DebtTab(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Surface(
        modifier =
            modifier.clickable {
                onClick()
            },

        color =
            if (selected) {
                MaterialTheme
                    .colorScheme
                    .surface
            } else {
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            },

        shape =
            MaterialTheme
                .shapes
                .medium,

        shadowElevation =
            if (selected) {
                2.dp
            } else {
                0.dp
            }
    ) {

        Box(
            modifier =
                Modifier.padding(
                    vertical = 11.dp
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = text,

                style =
                    MaterialTheme.typography
                        .labelLarge,

                color =
                    if (selected) {
                        MaterialTheme
                            .colorScheme
                            .primary
                    } else {
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                    }
            )
        }
    }
}