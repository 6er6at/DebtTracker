package com.example.debttracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit
) {

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val formatter =
        DateTimeFormatter.ofPattern(
            "dd.MM.yyyy"
        )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDatePicker = true
            }
    ) {

        OutlinedTextField(
            value =
                selectedDate?.format(formatter)
                    ?: "",

            onValueChange = {},

            readOnly = true,
            enabled = false,

            label = {
                Text("Дата возврата")
            },

            colors =
                OutlinedTextFieldDefaults.colors(
                    disabledTextColor =
                        MaterialTheme
                            .colorScheme
                            .onSurface,

                    disabledLabelColor =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    disabledBorderColor =
                        MaterialTheme
                            .colorScheme
                            .primary
                ),

            modifier =
                Modifier.fillMaxWidth()
        )
    }

    if (showDatePicker) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    selectedDate
                        ?.atStartOfDay(
                            ZoneOffset.UTC
                        )
                        ?.toInstant()
                        ?.toEpochMilli()
            )

        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val millis =
                            datePickerState
                                .selectedDateMillis

                        if (millis != null) {

                            val date =
                                Instant
                                    .ofEpochMilli(
                                        millis
                                    )
                                    .atZone(
                                        ZoneOffset.UTC
                                    )
                                    .toLocalDate()

                            onDateSelected(
                                date
                            )
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Готово")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Отмена")
                }
            }

        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }
}