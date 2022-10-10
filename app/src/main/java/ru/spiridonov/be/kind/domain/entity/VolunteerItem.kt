package ru.spiridonov.be.kind.domain.entity

data class VolunteerItem(
    val uuid: String,
    val surName: String,
    val name: String,
    val lastname: String,
    var personalPhone: String,
    var email: String,
    val birthday:  String? = null,
    var city: String,
    var helpReason: List<String>,
    var photoUrl: String? = null,
    @field:JvmField
    var isAccountConfirmed: Boolean = false,
    @field:JvmField
    var isPassportConfirmed: Boolean = false,
    @field:JvmField
    var isCertOfMedicalEduConfirmed: Boolean = false,
) {
    constructor() : this(
        uuid = "",
        surName = "",
        name = "",
        lastname = "",
        personalPhone = "",
        email = "",
        birthday = null,
        city = "",
        helpReason = listOf(),
        photoUrl = null,
        isAccountConfirmed = false,
        isPassportConfirmed = false,
        isCertOfMedicalEduConfirmed = false
    )
}
