package com.example.debttracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier


@Composable
fun ReminderSelector(
    selected: ReminderDays?,
    onSelected: (ReminderDays?) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val currentText =
        when (selected) {

            null ->
                "Не напоминать"

            ReminderDays.ONE ->
                "За 1 день"

            ReminderDays.THREE ->
                "За 3 дня"

            ReminderDays.FIVE ->
                "За 5 дней"

            ReminderDays.SEVEN ->
                "За 7 дней"

            ReminderDays.TEN ->
                "За 10 дней"
        }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        Button(
            onClick = {
                expanded = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Напоминание: $currentText"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            DropdownMenuItem(
                text = {
                    Text("Не напоминать")
                },
                onClick = {
                    onSelected(null)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("За 1 день")
                },
                onClick = {
                    onSelected(ReminderDays.ONE)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("За 3 дня")
                },
                onClick = {
                    onSelected(ReminderDays.THREE)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("За 5 дней")
                },
                onClick = {
                    onSelected(ReminderDays.FIVE)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("За 7 дней")
                },
                onClick = {
                    onSelected(ReminderDays.SEVEN)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("За 10 дней")
                },
                onClick = {
                    onSelected(ReminderDays.TEN)
                    expanded = false
                }
            )
        }
    }
}