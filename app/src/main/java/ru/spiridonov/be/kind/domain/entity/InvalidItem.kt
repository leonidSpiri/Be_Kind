package ru.spiridonov.be.kind.domain.entity

data class InvalidItem(
    val uuid: String,
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
    var photoUrl: String? = null,
    var passportGeneralUrl: String? = null,
    var passportRegistrationUrl: String? = null,
    var certOfDisabilityUrl: String? = null,
    @field:JvmField
    var isAccountConfirmed: Boolean = false
) {
    constructor() : this(
        uuid = "",
        surName = "",
        name = "",
        lastname = "",
        personalPhone = "",
        relativePhone = null,
        email = "",
        birthday = "",
        city = "",
        address = null,
        helpReason = listOf(),
        photoUrl = null,
        passportGeneralUrl = null,
        passportRegistrationUrl = null,
        certOfDisabilityUrl = null,
        isAccountConfirmed = false
    )
}
