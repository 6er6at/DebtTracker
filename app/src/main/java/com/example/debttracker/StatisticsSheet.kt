package com.example.debttracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsSheet(
    debts: List<Debt>,
    history: List<DebtHistory>,
    onDismiss: () -> Unit
) {
    var selectedType by remember {
        mutableStateOf(DebtType.OWED_TO_ME)
    }

    val stats = calculateDebtStatistics(
        debts = debts,
        history = history,
        type = selectedType
    )

    val owedToMe = debts.filter {
        it.type == DebtType.OWED_TO_ME && it.status == DebtStatus.ACTIVE
    }.sumOf { it.amount }

    val iOwe = debts.filter {
        it.type == DebtType.I_OWE && it.status == DebtStatus.ACTIVE
    }.sumOf { it.amount }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "По всем долгам, независимо от поиска и сортировки",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))

            StatMetric(
                label = "Баланс активных долгов (мне должны − я должен)",
                value = rubles(owedToMe - iOwe),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedType == DebtType.OWED_TO_ME,
                    onClick = { selectedType = DebtType.OWED_TO_ME },
                    label = { Text("Мне должны") }
                )
                FilterChip(
                    selected = selectedType == DebtType.I_OWE,
                    onClick = { selectedType = DebtType.I_OWE },
                    label = { Text("Я должен") }
                )
            }
            Spacer(Modifier.height(12.dp))

            StatMetric(
                label = "Текущий остаток",
                value = rubles(stats.outstandingAmount),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetric(
                    label = "Активных",
                    value = stats.activeCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatMetric(
                    label = "Погашено полностью",
                    value = stats.paidCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetric(
                    label = "Просрочено",
                    value = stats.overdueCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatMetric(
                    label = "Уменьшений суммы",
                    value = stats.decreaseOperations.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))
            StatMetric(
                label = "Всего уменьшено по истории",
                value = rubles(stats.decreaseAmount),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Уменьшения включают частичные возвраты и ручные корректировки. Удалённые долги не учитываются.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(28.dp))
        }
    }
}

private fun rubles(value: Long): String =
    String.format(Locale.forLanguageTag("ru-RU"), "%,d ₽", value)

@Composable
private fun StatMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
