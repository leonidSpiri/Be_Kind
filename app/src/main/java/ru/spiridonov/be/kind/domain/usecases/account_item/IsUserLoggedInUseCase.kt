package ru.spiridonov.be.kind.domain.usecases.account_item

import ru.spiridonov.be.kind.domain.repository.AccountRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke() = repository.isUserLoggedIn()
}