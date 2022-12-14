package ru.spiridonov.be.kind.domain.entity

data class AccountItem(
    val type: String,
    val surName: String? = null,
    val password: String,
    val name: String? = null,
    val lastname: String? = null,
    var personalPhone: String? = null,
    var email: String,
    val gender: String? = null,
    val birthday: String? = null,
    var city: String? = null,
    var relativePhone: String? = null,
    var helpReason: List<String>? = null,
    var isAccountConfirmed: Boolean = false,
    var isPassportConfirmed: Boolean = false,
    var isCertConfirmed: Boolean = false,
)