package com.example.debttracker

import androidx.compose.animation.core.animateDpAsState

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = debt.personName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                DebtStatusBadge(
                    status = debt.status
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "${debt.amount} ₽",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
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
                    }",

                    style = MaterialTheme.typography.bodyMedium,

                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!debt.comment.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Комментарий:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = debt.comment!!,
                    style = MaterialTheme.typography.bodyMedium
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (history.isEmpty()) {

                Text(
                    text = "История пока пуста",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),

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

    val operationColor =
        if (history.amountChange > 0) {
            Color(0xFFDC2626)
        } else {
            Color(0xFF16A34A)
        }

    val sign =
        if (history.amountChange > 0) {
            "+"
        } else {
            ""
        }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text =
                    "$sign${history.amountChange} ₽",

                style =
                    MaterialTheme.typography.titleMedium,

                fontWeight =
                    FontWeight.Bold,

                color =
                    operationColor
            )

            Text(
                text =
                    "Остаток: ${history.balanceAfter} ₽",

                style =
                    MaterialTheme.typography.bodyMedium
            )

            Text(
                text =
                    history.changedAt.format(
                        DateTimeFormatter.ofPattern(
                            "dd.MM.yyyy HH:mm"
                        )
                    ),

                style =
                    MaterialTheme.typography.labelSmall,

                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )

            if (!history.comment.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = history.comment!!,

                    style =
                        MaterialTheme.typography.bodyMedium
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
                    modifier = Modifier.height(16.dp)
                )

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(
                            RoundedCornerShape(14.dp)
                        )
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                        .padding(4.dp)
                ) {

                    val segmentWidth =
                        maxWidth / 2

                    val indicatorOffset by
                    animateDpAsState(
                        targetValue =
                            if (increase) {
                                0.dp
                            } else {
                                segmentWidth
                            },
                        label =
                            "operationIndicator"
                    )

                    Box(
                        modifier = Modifier
                            .width(segmentWidth)
                            .fillMaxHeight()
                            .offset(
                                x = indicatorOffset
                            )
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                            .background(
                                MaterialTheme.colorScheme.surface
                            )
                    )

                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    increase = true
                                },

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text = "+  Увеличить",

                                color =
                                    if (increase) {
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                    } else {
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                    },

                                fontWeight =
                                    if (increase) {
                                        FontWeight.SemiBold
                                    } else {
                                        FontWeight.Normal
                                    }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    increase = false
                                },

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text = "−  Уменьшить",

                                color =
                                    if (!increase) {
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                    } else {
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                    },

                                fontWeight =
                                    if (!increase) {
                                        FontWeight.SemiBold
                                    } else {
                                        FontWeight.Normal
                                    }
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
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
                        Text("Комментарий к операции")
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
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
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
            debt.returnDate
        )
    }

    var reminderDays by remember {
        mutableStateOf(
            debt.reminderDays
        )
    }

    var comment by remember {
        mutableStateOf(
            debt.comment ?: ""
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Изменить долг")
        },

        text = {

            Column(
                modifier =
                    Modifier.verticalScroll(
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

                DebtDatePickerField(
                    selectedDate =
                        returnDate,

                    onDateSelected = {
                        returnDate = it
                    }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                ReminderSelector(
                    selected =
                        reminderDays,

                    onSelected = {
                        reminderDays = it
                    }
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

                        val updatedDebt =
                            debt.copy(
                                personName =
                                    personName.trim(),

                                amount =
                                    parsedAmount,

                                returnDate =
                                    returnDate,

                                reminderDays =
                                    reminderDays,

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
        mutableStateOf<LocalDate?>(null)
    }

    var reminderDays by remember {
        mutableStateOf<ReminderDays?>(null)
    }

    var comment by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Добавить долг")
        },

        text = {

            Column(
                modifier =
                    Modifier.verticalScroll(
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

                DebtDatePickerField(
                    selectedDate =
                        returnDate,

                    onDateSelected = {
                        returnDate = it
                    }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                ReminderSelector(
                    selected =
                        reminderDays,

                    onSelected = {
                        reminderDays = it
                    }
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
                                    returnDate,

                                comment =
                                    comment.ifBlank {
                                        null
                                    },

                                status =
                                    DebtStatus.ACTIVE,

                                reminderDays =
                                    reminderDays
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