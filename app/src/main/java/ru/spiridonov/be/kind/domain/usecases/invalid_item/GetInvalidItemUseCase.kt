package ru.spiridonov.be.kind.domain.usecases.invalid_item

import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.repository.InvalidItemRepository
import javax.inject.Inject

class GetInvalidItemUseCase @Inject constructor(
    private val repository: InvalidItemRepository
) {
    operator fun invoke(callback: (InvalidItem?) -> Unit) = repository.getInvalidItem(callback)
}