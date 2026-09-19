package com.example.debttracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight


import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface

import java.time.format.DateTimeFormatter


@Composable
fun DebtList(
    debts: List<Debt>,
    onDebtClick: (Debt) -> Unit,
    modifier: Modifier = Modifier
) {

    if (debts.isEmpty()) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    start = 8.dp,
                    end = 8.dp
                )
        ) {

            Text(
                text = "Долгов пока нет",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    } else {

        LazyColumn(
            modifier = modifier.fillMaxWidth(),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            items(
                items = debts,
                key = {
                    it.id
                }
            ) { debt ->

                DebtItem(
                    debt = debt,
                    onClick = {
                        onDebtClick(debt)
                    }
                )
            }
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
            },

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // Верхняя строка:
            // имя + статус
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = debt.personName,

                    style =
                        MaterialTheme.typography.titleLarge,

                    fontWeight =
                        FontWeight.Bold,

                    modifier = Modifier.weight(1f)
                )

                DebtStatusBadge(
                    status = debt.status
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            // Тип долга
            Text(
                text =
                    if (debt.type == DebtType.OWED_TO_ME) {
                        "Мне должны"
                    } else {
                        "Я должен"
                    },

                style =
                    MaterialTheme.typography.bodySmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Сумма
            Text(
                text = "${debt.amount} ₽",

                style =
                    MaterialTheme.typography
                        .headlineSmall,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            )

            // Дата возврата
            if (debt.returnDate != null) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Вернуть до: ${
                        debt.returnDate.format(
                            DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy"
                            )
                        )
                    }",

                    style =
                        MaterialTheme.typography.bodyMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            // Комментарий
            if (!debt.comment.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = debt.comment!!,

                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun DebtStatusBadge(
    status: DebtStatus
) {

    val containerColor =
        if (status == DebtStatus.PAID) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }

    val textColor =
        if (status == DebtStatus.PAID) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onPrimaryContainer
        }

    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small
    ) {

        Text(
            text =
                if (status == DebtStatus.PAID) {
                    "Погашен"
                } else {
                    "Активен"
                },

            style =
                MaterialTheme.typography.labelSmall,

            fontWeight = FontWeight.SemiBold,

            color = textColor,

            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
        )
    }
}