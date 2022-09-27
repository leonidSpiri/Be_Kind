package ru.spiridonov.be.kind.domain.entity

data class VolunteerItem(
    val uuid: String,
    val surName: String,
    val name: String,
    val lastname: String,
    var personalPhone: String,
    var email: String,
    val birthday: String,
    var city: String,
    var helpReason: List<String>,
    var photoUrl: String? = null,
    var passportGeneralUrl: String? = null,
    var passportRegistrationUrl: String? = null,
    var certOfMedicalEduUrl: String? = null,
    var isAccountConfirmed: Boolean = false
)
