package ru.spiridonov.be.kind.data.repository

import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.repository.InvalidItemRepository
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class InvalidItemRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref
) : InvalidItemRepository {
    override suspend fun editInvalidItem(invalidItem: InvalidItem) {
        sharedPref.setInvalidAccountInfo(invalidItem)
    }

    override fun getInvalidItem(): InvalidItem? {
        val item = sharedPref.getInvalidAccountInfo()
        if (item.uuid.isEmpty()) return null
        return item
    }
}