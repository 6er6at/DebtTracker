package com.example.debttracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val debtId =
            intent.getLongExtra(
                "extra_debt_id",
                -1L
            )

        if (debtId == -1L) {
            return
        }

        val pendingResult =
            goAsync()

        CoroutineScope(
            Dispatchers.IO
        ).launch {

            try {

                val database =
                    DatabaseProvider
                        .getDatabase(context)

                val debt =
                    database
                        .debtDao()
                        .getDebtById(debtId)

                if (
                    debt != null &&
                    debt.status ==
                    DebtStatus.ACTIVE
                ) {

                    NotificationHelper
                        .showDebtReminder(
                            context,
                            debt
                        )
                }

            } finally {

                pendingResult.finish()
            }
        }
    }
}