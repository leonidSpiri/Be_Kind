package ru.spiridonov.be.kind.domain.entity

data class WorkItem(
    val id: Int,
    var description: String,
    var isDone: Boolean,
    val whoNeedHelpId: Int,
    val timestamp: Long,
    val whenNeedHelp: Long,
    val kindOfHelp: String,
    val startCoordinates: String,
    val address: String,
    var realCoordinatesInvalid: String,
    val degreeOfVolunteer: Int,
    val needMedicineCertificate: Boolean,
    val whoHelpId: Int,
    var realCoordinatesVolunteer: String,
    )
