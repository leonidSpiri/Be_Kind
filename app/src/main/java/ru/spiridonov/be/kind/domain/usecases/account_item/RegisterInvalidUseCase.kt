package ru.spiridonov.be.kind.domain.usecases.account_item

import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import javax.inject.Inject

class RegisterInvalidUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(invalidItem: InvalidItem) = repository.registerInvalid(invalidItem)
}