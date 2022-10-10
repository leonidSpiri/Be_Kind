package ru.spiridonov.be.kind.domain.entity

data class InvalidItem(
    val uuid: String,
    val surName: String,
    val name: String,
    val lastname: String,
    var personalPhone: String,
    var relativePhone: String? = null,
    var email: String,
    val birthday:  String? = null,
    var city: String,
    var address: String? = null,
    var helpReason: List<String>,
    var photoUrl: String? = null,
    @field:JvmField
    var isAccountConfirmed: Boolean = false,
    @field:JvmField
    var isPassportConfirmed: Boolean = false,
    @field:JvmField
    var isCertOfDisabilityConfirmed: Boolean = false,
) {
    constructor() : this(
        uuid = "",
        surName = "",
        name = "",
        lastname = "",
        personalPhone = "",
        relativePhone = null,
        email = "",
        birthday = null,
        city = "",
        address = null,
        helpReason = listOf(),
        photoUrl = null,
        isAccountConfirmed = false,
        isPassportConfirmed = false,
        isCertOfDisabilityConfirmed = false
    )
}
