package ru.spiridonov.be.kind.data.repository

import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.VolunteerItemRepository
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class VolunteerItemRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref
) : VolunteerItemRepository {
    override suspend fun editVolunteerItem(VolunteerItem: VolunteerItem) {
        sharedPref.setVolunteerAccountInfo(VolunteerItem)
    }

    override fun getVolunteerItem(): VolunteerItem? {
        val item = sharedPref.getVolunteerAccountInfo()
      //  if (item.uuid.isEmpty()) return null
        return item
    }
}