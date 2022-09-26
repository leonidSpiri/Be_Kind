package ru.spiridonov.be.kind.domain.usecases.invalid_item

import ru.spiridonov.be.kind.domain.repository.InvalidItemRepository
import javax.inject.Inject

class GetInvalidItemUseCase @Inject constructor(
    private val repository: InvalidItemRepository
) {
    operator fun invoke() = repository.getInvalidItem()
}