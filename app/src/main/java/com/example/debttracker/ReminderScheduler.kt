package com.example.debttracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


object ReminderScheduler {

    private const val EXTRA_DEBT_ID =
        "extra_debt_id"

    private const val REMINDER_HOUR =
        9

    private const val REMINDER_MINUTE =
        0


    fun schedule(
        context: Context,
        debt: Debt
    ) {

        cancel(
            context,
            debt.id
        )

        if (
            debt.status != DebtStatus.ACTIVE ||
            debt.returnDate == null ||
            debt.reminderDays == null
        ) {
            return
        }

        val reminderDate =
            debt.returnDate.minusDays(
                debt.reminderDays.days.toLong()
            )

        val reminderDateTime =
            reminderDate.atTime(
                LocalTime.of(
                    REMINDER_HOUR,
                    REMINDER_MINUTE
                )
            )

        val triggerMillis =
            reminderDateTime
                .atZone(
                    ZoneId.systemDefault()
                )
                .toInstant()
                .toEpochMilli()

        if (
            triggerMillis <=
            System.currentTimeMillis()
        ) {
            return
        }

        val intent =
            Intent(
                context,
                ReminderReceiver::class.java
            ).apply {

                putExtra(
                    EXTRA_DEBT_ID,
                    debt.id
                )
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode(debt.id),
                intent,

                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pendingIntent
        )
    }


    fun cancel(
        context: Context,
        debtId: Long
    ) {

        val intent =
            Intent(
                context,
                ReminderReceiver::class.java
            )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode(debtId),
                intent,

                PendingIntent.FLAG_NO_CREATE or
                        PendingIntent.FLAG_IMMUTABLE
            )

        if (pendingIntent != null) {

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            alarmManager.cancel(
                pendingIntent
            )

            pendingIntent.cancel()
        }
    }


    private fun requestCode(
        debtId: Long
    ): Int {

        return (
                debtId xor
                        (debtId ushr 32)
                ).toInt()
    }


    suspend fun rescheduleAll(
        context: Context
    ) {

        val database =
            DatabaseProvider.getDatabase(
                context
            )

        val debts =
            database
                .debtDao()
                .getAllDebts()

        debts.forEach { debt ->

            schedule(
                context,
                debt
            )
        }
    }
}