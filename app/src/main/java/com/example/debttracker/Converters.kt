package com.example.debttracker

import androidx.room3.ColumnTypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

object Converters {

    @ColumnTypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @ColumnTypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @ColumnTypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @ColumnTypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
}