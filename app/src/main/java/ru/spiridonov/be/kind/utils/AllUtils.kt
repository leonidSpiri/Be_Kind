package ru.spiridonov.be.kind.utils

import java.text.SimpleDateFormat
import java.util.*

class AllUtils {
    fun isItPhone(phone: String): Boolean {
        val regex = Regex("^(\\+7|8)\\d{10}\$")
        return regex.matches(phone)
    }

    fun stringToDateLong(date: String) =
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            sdf.parse(date)?.time ?: -1L
        } catch (e: Exception) {
            -1L
        }

    fun dateLongToString(date: Long) =
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            sdf.format(Date(date))
        } catch (e: Exception) {
            ""
        }

    fun getRealDateHour(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }
}