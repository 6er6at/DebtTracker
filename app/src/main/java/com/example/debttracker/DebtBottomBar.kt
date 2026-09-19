package com.example.debttracker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DebtBottomBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onSettingsClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),

        color = MaterialTheme.colorScheme.surface,

        shadowElevation = 8.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onSettingsClick
            ) {

                Text(
                    text = "⚙",
                    fontSize = 22.sp
                )
            }

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            OutlinedTextField(
                value = searchQuery,

                onValueChange =
                    onSearchChanged,

                label = {
                    Text("Поиск")
                },

                placeholder = {
                    Text(
                        "Имя или комментарий"
                    )
                },

                singleLine = true,

                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}