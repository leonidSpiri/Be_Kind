package ru.spiridonov.be.kind.domain.repository

import ru.spiridonov.be.kind.domain.entity.VolunteerItem

interface VolunteerItemRepository {

    suspend fun editVolunteerItem(volunteerItem: VolunteerItem, callback: (VolunteerItem?) -> Unit)

    fun getVolunteerItem(callback: (VolunteerItem?) -> Unit)
}