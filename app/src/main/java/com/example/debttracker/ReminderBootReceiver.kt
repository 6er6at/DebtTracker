package com.example.debttracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ReminderBootReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (
            intent.action !=
            Intent.ACTION_BOOT_COMPLETED
        ) {
            return
        }

        val pendingResult =
            goAsync()

        CoroutineScope(
            Dispatchers.IO
        ).launch {

            try {

                ReminderScheduler
                    .rescheduleAll(
                        context
                    )

            } finally {

                pendingResult.finish()
            }
        }
    }
}