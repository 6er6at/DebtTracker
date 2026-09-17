package com.example.debttracker

import androidx.room3.ColumnTypeConverter
import java.time.LocalDate

object Converters {

    @ColumnTypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @ColumnTypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}