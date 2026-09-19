package com.example.debttracker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DebtBottomBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onSettingsClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        TextButton(
            onClick = onSettingsClick
        ) {

            Text("⚙")
        }

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        OutlinedTextField(
            value = searchQuery,

            onValueChange = onSearchChanged,

            label = {
                Text("Поиск")
            },

            placeholder = {
                Text("Имя или комментарий")
            },

            singleLine = true,

            modifier = Modifier
                .weight(1f)
        )
    }
}