package ru.spiridonov.be.kind.domain.usecases.invalid_item

import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.repository.InvalidItemRepository
import javax.inject.Inject

class EditInvalidItemUseCase @Inject constructor(
    private val repository: InvalidItemRepository
) {
    suspend operator fun invoke(invalidItem: InvalidItem, callback: (InvalidItem?) -> Unit) =
        repository.editInvalidItem(invalidItem, callback)
}