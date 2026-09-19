package com.example.debttracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

import androidx.core.app.NotificationCompat


object NotificationHelper {

    const val CHANNEL_ID = "debt_reminders"

    fun createNotificationChannel(
        context: Context
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Напоминания о долгах",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    "Напоминания о приближении даты возврата долга"
            }

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    fun showDebtReminder(
        context: Context,
        debt: Debt
    ) {

        createNotificationChannel(context)

        val title = "Напоминание о долге"

        val text =
            if (debt.type == DebtType.OWED_TO_ME) {

                "${debt.personName} должен вам ${debt.amount} ₽"

            } else {

                "Вы должны ${debt.personName} ${debt.amount} ₽"
            }

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(text)
                )
                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )
                .setAutoCancel(true)
                .build()

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        manager.notify(
            debt.id.hashCode(),
            notification
        )
    }
}