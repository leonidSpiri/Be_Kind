package ru.spiridonov.be.kind.domain.entity

data class WorkItem(
    var id: String,
    var description: String,
    var isDone: Boolean = false,
    var status: String,
    val whoNeedHelpId: String,
    val timestamp: Long,
    val whenNeedHelp: Long,
    val kindOfHelp: String,
    val startCoordinates: String? = null,
    val address: String,
    val invalidPhone: String,
    val volunteerPhone: String? = null,
    val volunteerGender: String,
    val volunteerAge: String,
    var realCoordinatesInvalid: String? = null,
    val degreeOfVolunteer: Int? = null,
    val needMedicineCertificate: Boolean = false,
    val whoHelpId: String? = null,
    val doneCode: String,
    var realCoordinatesVolunteer: String? = null
)
