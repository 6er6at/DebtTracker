package com.example.debttracker

import android.app.DatePickerDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDetailsSheet(
    debt: Debt,
    history: List<DebtHistory>,

    onEdit: () -> Unit,
    onAmountChange: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss
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
                text = debt.personName,
                fontSize = 26.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${debt.amount} ₽",
                fontSize = 24.sp
            )

            if (debt.returnDate != null) {

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Вернуть до: ${
                        debt.returnDate.format(
                            DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy"
                            )
                        )
                    }"
                )
            }

            if (!debt.comment.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Комментарий:",
                    fontSize = 15.sp
                )

                Text(
                    text = debt.comment!!,
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onAmountChange,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Изменить сумму")
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Редактировать")
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Удалить")
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "История",
                fontSize = 22.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (history.isEmpty()) {

                Text(
                    text = "История пока пуста"
                )

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            max = 280.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(history) { item ->

                        HistoryItem(item)
                    }
                }
            }
        }
    }
}


@Composable
fun HistoryItem(
    history: DebtHistory
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            val sign =
                if (history.amountChange > 0) {
                    "+"
                } else {
                    ""
                }

            Text(
                text =
                    "$sign${history.amountChange} ₽",
                fontSize = 18.sp
            )

            Text(
                text =
                    "Остаток: ${history.balanceAfter} ₽"
            )

            Text(
                text =
                    history.changedAt.format(
                        DateTimeFormatter.ofPattern(
                            "dd.MM.yyyy HH:mm"
                        )
                    ),

                fontSize = 13.sp
            )

            if (!history.comment.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = history.comment!!,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Composable
fun AmountChangeDialog(
    debt: Debt,
    onDismiss: () -> Unit,
    onSave: (Long, String?) -> Unit
) {

    var amount by remember {
        mutableStateOf("")
    }

    var comment by remember {
        mutableStateOf("")
    }

    var increase by remember {
        mutableStateOf(true)
    }

    var errorText by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Изменить сумму")
        },

        text = {

            Column {

                Text(
                    text =
                        "Текущий долг: ${debt.amount} ₽"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = {
                            increase = true
                        },
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text("+ Увеличить")
                    }

                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )

                    Button(
                        onClick = {
                            increase = false
                        },
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text("− Уменьшить")
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = amount,

                    onValueChange = {
                        amount = it
                        errorText = ""
                    },

                    label = {
                        Text("Сумма")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = comment,

                    onValueChange = {
                        comment = it
                    },

                    label = {
                        Text(
                            "Комментарий к операции"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                if (errorText.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = errorText,
                        color = Color.Red
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val value =
                        amount.toLongOrNull()

                    if (
                        value == null ||
                        value <= 0
                    ) {

                        errorText =
                            "Введите положительную сумму"

                        return@Button
                    }

                    if (
                        !increase &&
                        value > debt.amount
                    ) {

                        errorText =
                            "Нельзя уменьшить долг больше текущей суммы"

                        return@Button
                    }

                    val change =
                        if (increase) {
                            value
                        } else {
                            -value
                        }

                    onSave(
                        change,
                        comment.ifBlank {
                            null
                        }
                    )
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
fun EditDebtDialog(
    debt: Debt,
    viewModel: DebtViewModel,
    onDismiss: () -> Unit
) {

    var personName by remember {
        mutableStateOf(
            debt.personName
        )
    }

    var amount by remember {
        mutableStateOf(
            debt.amount.toString()
        )
    }

    var returnDate by remember {

        mutableStateOf(
            debt.returnDate?.format(
                DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy"
                )
            ) ?: ""
        )
    }

    var comment by remember {
        mutableStateOf(
            debt.comment ?: ""
        )
    }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Изменить долг")
        },

        text = {

            Column(
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )
            ) {

                OutlinedTextField(
                    value = personName,

                    onValueChange = {
                        personName = it
                    },

                    label = {
                        Text("Имя")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = amount,

                    onValueChange = {
                        amount = it
                    },

                    label = {
                        Text("Сумма в рублях")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            val calendar =
                                Calendar.getInstance()

                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->

                                    returnDate =
                                        String.format(
                                            "%02d.%02d.%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                        )
                                },

                                calendar.get(
                                    Calendar.YEAR
                                ),

                                calendar.get(
                                    Calendar.MONTH
                                ),

                                calendar.get(
                                    Calendar.DAY_OF_MONTH
                                )
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

                        colors =
                            OutlinedTextFieldDefaults
                                .colors(
                                    disabledTextColor =
                                        Color.Black,

                                    disabledLabelColor =
                                        Color(0xFF6750A4),

                                    disabledBorderColor =
                                        Color(0xFF6750A4)
                                ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = comment,

                    onValueChange = {
                        comment = it
                    },

                    label = {
                        Text("Комментарий")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val parsedAmount =
                        amount.toLongOrNull()

                    if (
                        personName.isNotBlank() &&
                        parsedAmount != null &&
                        parsedAmount > 0
                    ) {

                        val parsedDate =
                            if (returnDate.isNotBlank()) {

                                LocalDate.parse(
                                    returnDate,

                                    DateTimeFormatter.ofPattern(
                                        "dd.MM.yyyy"
                                    )
                                )

                            } else {
                                null
                            }

                        val updatedDebt =
                            debt.copy(
                                personName =
                                    personName.trim(),

                                amount =
                                    parsedAmount,

                                returnDate =
                                    parsedDate,

                                comment =
                                    comment.ifBlank {
                                        null
                                    }
                            )

                        viewModel.updateDebt(
                            updatedDebt
                        )

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

    var comment by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Добавить долг")
        },

        text = {

            Column(
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )
            ) {

                OutlinedTextField(
                    value = personName,

                    onValueChange = {
                        personName = it
                    },

                    label = {
                        Text("Имя")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = amount,

                    onValueChange = {
                        amount = it
                    },

                    label = {
                        Text("Сумма в рублях")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            val calendar =
                                Calendar.getInstance()

                            DatePickerDialog(
                                context,

                                { _, year, month, dayOfMonth ->

                                    returnDate =
                                        String.format(
                                            "%02d.%02d.%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                        )
                                },

                                calendar.get(
                                    Calendar.YEAR
                                ),

                                calendar.get(
                                    Calendar.MONTH
                                ),

                                calendar.get(
                                    Calendar.DAY_OF_MONTH
                                )
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

                        colors =
                            OutlinedTextFieldDefaults
                                .colors(
                                    disabledTextColor =
                                        Color.Black,

                                    disabledLabelColor =
                                        Color(0xFF6750A4),

                                    disabledBorderColor =
                                        Color(0xFF6750A4)
                                ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = comment,

                    onValueChange = {
                        comment = it
                    },

                    label = {
                        Text("Комментарий")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val parsedAmount =
                        amount.toLongOrNull()

                    if (
                        personName.isNotBlank() &&
                        parsedAmount != null &&
                        parsedAmount > 0
                    ) {

                        val parsedDate =
                            if (returnDate.isNotBlank()) {

                                LocalDate.parse(
                                    returnDate,

                                    DateTimeFormatter.ofPattern(
                                        "dd.MM.yyyy"
                                    )
                                )

                            } else {
                                null
                            }

                        val debt =
                            Debt(
                                id =
                                    System.currentTimeMillis(),

                                personName =
                                    personName.trim(),

                                amount =
                                    parsedAmount,

                                type =
                                    debtType,

                                returnDate =
                                    parsedDate,

                                comment =
                                    comment.ifBlank {
                                        null
                                    },

                                status =
                                    DebtStatus.ACTIVE,

                                reminderDays =
                                    null
                            )

                        viewModel.addDebt(
                            debt
                        )

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