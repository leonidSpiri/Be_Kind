package ru.spiridonov.be.kind.domain.repository

import ru.spiridonov.be.kind.domain.entity.VolunteerItem

interface VolunteerItemRepository {

    suspend fun editVolunteerItem(VolunteerItem: VolunteerItem)

    fun getVolunteerItem(): VolunteerItem?
}