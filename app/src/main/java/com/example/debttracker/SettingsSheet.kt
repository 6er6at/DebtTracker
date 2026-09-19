package com.example.debttracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    themeMode: ThemeMode,
    sortMode: SortMode,

    onThemeChanged: (ThemeMode) -> Unit,
    onSortChanged: (SortMode) -> Unit,

    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        ) {

            Text(
                text = "Настройки",
                fontSize = 26.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Тема",
                fontSize = 20.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            SettingsOption(
                text = "Системная",
                selected =
                    themeMode == ThemeMode.SYSTEM,
                onClick = {
                    onThemeChanged(
                        ThemeMode.SYSTEM
                    )
                }
            )

            SettingsOption(
                text = "Светлая",
                selected =
                    themeMode == ThemeMode.LIGHT,
                onClick = {
                    onThemeChanged(
                        ThemeMode.LIGHT
                    )
                }
            )

            SettingsOption(
                text = "Тёмная",
                selected =
                    themeMode == ThemeMode.DARK,
                onClick = {
                    onThemeChanged(
                        ThemeMode.DARK
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Сортировка",
                fontSize = 20.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            SettingsOption(
                text = "Без сортировки",
                selected =
                    sortMode == SortMode.NONE,
                onClick = {
                    onSortChanged(
                        SortMode.NONE
                    )
                }
            )

            SettingsOption(
                text = "Сумма: сначала больше",
                selected =
                    sortMode == SortMode.AMOUNT_DESC,
                onClick = {
                    onSortChanged(
                        SortMode.AMOUNT_DESC
                    )
                }
            )

            SettingsOption(
                text = "Сумма: сначала меньше",
                selected =
                    sortMode == SortMode.AMOUNT_ASC,
                onClick = {
                    onSortChanged(
                        SortMode.AMOUNT_ASC
                    )
                }
            )

            SettingsOption(
                text = "Дата возврата: ближайшая",
                selected =
                    sortMode == SortMode.DATE_ASC,
                onClick = {
                    onSortChanged(
                        SortMode.DATE_ASC
                    )
                }
            )

            SettingsOption(
                text = "Дата возврата: дальняя",
                selected =
                    sortMode == SortMode.DATE_DESC,
                onClick = {
                    onSortChanged(
                        SortMode.DATE_DESC
                    )
                }
            )

            SettingsOption(
                text = "Имя: А–Я",
                selected =
                    sortMode == SortMode.NAME_ASC,
                onClick = {
                    onSortChanged(
                        SortMode.NAME_ASC
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Закрыть")
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}


@Composable
private fun SettingsOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Text(
        text = if (selected) {
            "●  $text"
        } else {
            "○  $text"
        },

        fontSize = 17.sp,

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                vertical = 10.dp
            )
    )
}