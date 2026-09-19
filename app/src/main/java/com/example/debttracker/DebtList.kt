package com.example.debttracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Card
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                .padding(top = 16.dp)
        ) {

            Text(
                text = "Долгов пока нет",
                fontSize = 18.sp
            )
        }

    } else {

        LazyColumn(
            modifier = modifier.fillMaxWidth(),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)
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
                            DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy"
                            )
                        )
                    }"
                )
            }

            if (!debt.comment.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = debt.comment!!,
                    fontSize = 14.sp
                )
            }
        }
    }
}