package ru.spiridonov.be.kind.domain.usecases.account_item

import ru.spiridonov.be.kind.domain.repository.AccountRepository
import javax.inject.Inject

class LoginInvalidUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(login: String, password: String, callback: (Boolean, String) -> Unit) =
        repository.loginInvalid(login, password, callback)
}