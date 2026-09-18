package com.example.debttracker

import android.app.Application
import android.app.DatePickerDialog
import android.os.Bundle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.debttracker.ui.theme.DebtTrackerTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DebtTrackerTheme {
                DebtTrackerApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtTrackerApp() {

    var selectedTab by remember {
        mutableStateOf(0)
    }

    var showAddDebt by remember {
        mutableStateOf(false)
    }

    var selectedDebt by remember {
        mutableStateOf<Debt?>(null)
    }

    var showEditDebt by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val debtViewModel: DebtViewModel = viewModel(
        factory = AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    val debts by debtViewModel.debts.collectAsState()

    val filteredDebts = debts.filter { debt ->

        if (selectedTab == 0) {
            debt.type == DebtType.OWED_TO_ME
        } else {
            debt.type == DebtType.I_OWE
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Учёт долгов",
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Контроль ваших долгов в одном месте",
                fontSize = 20.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
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
                    horizontalAlignment = Alignment.CenterHorizontally
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
                                .background(Color.Black)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTab = 1
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
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
                                .background(Color.Black)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Долгов в базе: ${filteredDebts.size}",
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (filteredDebts.isEmpty()) {

                Text(
                    text = "Долгов пока нет",
                    fontSize = 18.sp
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(filteredDebts) { debt ->

                        DebtItem(
                            debt = debt,
                            onClick = {
                                selectedDebt = debt
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                showAddDebt = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {

            Text("Добавить долг")
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

        if (selectedDebt != null && !showEditDebt) {

            ModalBottomSheet(
                onDismissRequest = {
                    selectedDebt = null
                }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 32.dp
                        )
                ) {

                    Text(
                        text = selectedDebt!!.personName,
                        fontSize = 22.sp
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Button(
                        onClick = {
                            showEditDebt = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Изменить")
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick = {

                            debtViewModel.deleteDebt(
                                selectedDebt!!
                            )

                            selectedDebt = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Удалить")
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick = {
                            selectedDebt = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Отмена")
                    }
                }
            }
        }

        if (selectedDebt != null && showEditDebt) {

            EditDebtDialog(
                debt = selectedDebt!!,
                viewModel = debtViewModel,
                onDismiss = {
                    showEditDebt = false
                    selectedDebt = null
                }
            )
        }
    }
}


@Composable
fun DebtItem(
    debt: Debt,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = debt.personName,
                fontSize = 20.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "${debt.amount} ₽",
                fontSize = 18.sp
            )

            if (debt.returnDate != null) {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Вернуть до: ${
                        debt.returnDate.format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        )
                    }"
                )
            }
        }
    }
}


@Composable
fun EditDebtDialog(
    debt: Debt,
    viewModel: DebtViewModel,
    onDismiss: () -> Unit
) {

    var personName by remember {
        mutableStateOf(debt.personName)
    }

    var amount by remember {
        mutableStateOf(debt.amount.toString())
    }

    var returnDate by remember {

        mutableStateOf(
            debt.returnDate?.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
            ) ?: ""
        )
    }

    val context = LocalContext.current

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Изменить долг")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = personName,
                    onValueChange = {
                        personName = it
                    },
                    label = {
                        Text("Имя")
                    }
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                    },
                    label = {
                        Text("Сумма в рублях")
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            val calendar = Calendar.getInstance()

                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->

                                    returnDate = String.format(
                                        "%02d.%02d.%04d",
                                        dayOfMonth,
                                        month + 1,
                                        year
                                    )
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                ) {

                    OutlinedTextField(
                        value = returnDate,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = {
                            Text("Дата возврата")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledLabelColor = Color(0xFF6750A4),
                            disabledBorderColor = Color(0xFF6750A4)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val parsedAmount = amount.toLongOrNull()

                    if (
                        personName.isNotBlank() &&
                        parsedAmount != null &&
                        parsedAmount > 0
                    ) {

                        val parsedDate = if (returnDate.isNotBlank()) {

                            LocalDate.parse(
                                returnDate,
                                DateTimeFormatter.ofPattern("dd.MM.yyyy")
                            )

                        } else {
                            null
                        }

                        val updatedDebt = debt.copy(
                            personName = personName.trim(),
                            amount = parsedAmount,
                            returnDate = parsedDate
                        )

                        viewModel.updateDebt(updatedDebt)

                        onDismiss()
                    }
                }
            ) {

                Text("Сохранить")
            }
        },

        dismissButton = {

            Button(
                onClick = onDismiss
            ) {

                Text("Отмена")
            }
        }
    )
}


@Composable
fun AddDebtDialog(
    debtType: DebtType,
    viewModel: DebtViewModel,
    onDismiss: () -> Unit
) {

    var personName by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var returnDate by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Добавить долг")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = personName,
                    onValueChange = {
                        personName = it
                    },
                    label = {
                        Text("Имя")
                    }
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                    },
                    label = {
                        Text("Сумма в рублях")
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            val calendar = Calendar.getInstance()

                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->

                                    returnDate = String.format(
                                        "%02d.%02d.%04d",
                                        dayOfMonth,
                                        month + 1,
                                        year
                                    )
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                ) {

                    OutlinedTextField(
                        value = returnDate,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = {
                            Text("Дата возврата")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledLabelColor = Color(0xFF6750A4),
                            disabledBorderColor = Color(0xFF6750A4)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val parsedAmount = amount.toLongOrNull()

                    if (
                        personName.isNotBlank() &&
                        parsedAmount != null &&
                        parsedAmount > 0
                    ) {

                        val parsedDate = if (returnDate.isNotBlank()) {

                            LocalDate.parse(
                                returnDate,
                                DateTimeFormatter.ofPattern("dd.MM.yyyy")
                            )

                        } else {
                            null
                        }

                        val debt = Debt(
                            id = System.currentTimeMillis(),
                            personName = personName.trim(),
                            amount = parsedAmount,
                            type = debtType,
                            returnDate = parsedDate,
                            comment = null,
                            status = DebtStatus.ACTIVE,
                            reminderDays = null
                        )

                        viewModel.addDebt(debt)

                        onDismiss()
                    }
                }
            ) {

                Text("Сохранить")
            }
        },

        dismissButton = {

            Button(
                onClick = onDismiss
            ) {

                Text("Отмена")
            }
        }
    )
}