package ru.spiridonov.be.kind.domain.usecases.account_item

import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import javax.inject.Inject

class RegisterVolunteerUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(volunteerItem: VolunteerItem) = repository.registerVolunteer(volunteerItem)
}