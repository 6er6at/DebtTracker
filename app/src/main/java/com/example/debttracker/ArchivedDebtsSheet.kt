package com.example.debttracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedDebtsSheet(
    debts: List<Debt>,
    onDebtClick: (Debt) -> Unit,
    onDismiss: () -> Unit
) {

    var selectedTab by remember {
        mutableStateOf(0)
    }

    val filteredDebts = debts.filter { debt ->

        if (selectedTab == 0) {
            debt.type == DebtType.OWED_TO_ME
        } else {
            debt.type == DebtType.I_OWE
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Архив",

                style =
                    MaterialTheme.typography
                        .headlineLarge
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Здесь хранятся погашенные долги",

                style =
                    MaterialTheme.typography
                        .bodyLarge,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),

                color =
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant,

                shape =
                    MaterialTheme
                        .shapes
                        .large
            ) {

                Row(
                    modifier =
                        Modifier.padding(4.dp)
                ) {

                    ArchiveTab(
                        text = "Мне должны",

                        selected =
                            selectedTab == 0,

                        modifier =
                            Modifier.weight(1f),

                        onClick = {
                            selectedTab = 0
                        }
                    )

                    ArchiveTab(
                        text = "Я должен",

                        selected =
                            selectedTab == 1,

                        modifier =
                            Modifier.weight(1f),

                        onClick = {
                            selectedTab = 1
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "Погашенных: ${filteredDebts.size}",

                    style =
                        MaterialTheme.typography
                            .titleMedium
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            if (filteredDebts.isEmpty()) {

                Text(
                    text = "Архив пока пуст",

                    style =
                        MaterialTheme.typography
                            .bodyLarge,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

            } else {

                LazyColumn(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        items = filteredDebts,
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

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}


@Composable
private fun ArchiveTab(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Surface(
        modifier =
            modifier.clickable {
                onClick()
            },

        color =
            if (selected) {
                MaterialTheme
                    .colorScheme
                    .surface
            } else {
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            },

        shape =
            MaterialTheme
                .shapes
                .medium,

        shadowElevation =
            if (selected) {
                2.dp
            } else {
                0.dp
            }
    ) {

        Box(
            modifier =
                Modifier.padding(
                    vertical = 11.dp
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = text,

                style =
                    MaterialTheme.typography
                        .labelLarge,

                color =
                    if (selected) {
                        MaterialTheme
                            .colorScheme
                            .primary
                    } else {
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                    }
            )
        }
    }
}