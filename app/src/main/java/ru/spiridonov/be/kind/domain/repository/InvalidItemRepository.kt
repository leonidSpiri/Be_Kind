package ru.spiridonov.be.kind.domain.repository

import ru.spiridonov.be.kind.domain.entity.InvalidItem

interface InvalidItemRepository {

    suspend fun editInvalidItem(invalidItem: InvalidItem)

    fun getInvalidItem(): InvalidItem
}