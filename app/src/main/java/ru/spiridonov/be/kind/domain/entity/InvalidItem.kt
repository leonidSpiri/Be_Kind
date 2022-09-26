package ru.spiridonov.be.kind.domain.entity

data class InvalidItem(
    val id: Int,
    val surName: String,
    val name: String,
    val lastname: String,
    var personalPhone: String,
    var relativePhone: String? = null,
    var email: String,
    val birthday: String,
    var city: String,
    var address: String? = null,
    var helpReason: List<String>,
    var photoUrl: String,
    var passportGeneralUrl: String? = null,
    var passportRegistrationUrl: String? = null,
    var certOfDisabilityUrl: String? = null,
    var isAccountConfirmed: Boolean = false
)
