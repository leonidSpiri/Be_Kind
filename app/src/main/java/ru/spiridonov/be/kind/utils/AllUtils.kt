package ru.spiridonov.be.kind.utils

class AllUtils {
    fun isItPhone(phone: String): Boolean {
        val regex = Regex("^(\\+7|8)\\d{10}\$")
        return regex.matches(phone)
    }
}