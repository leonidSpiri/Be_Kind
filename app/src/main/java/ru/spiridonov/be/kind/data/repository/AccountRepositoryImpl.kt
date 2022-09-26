package ru.spiridonov.be.kind.data.repository

import android.app.Application
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    application: Application
) : AccountRepository {
    override fun loginInvalid(login: String, password: String): InvalidItem {
        TODO("Not yet implemented")
    }

    override fun loginVolunteer(login: String, password: String): VolunteerItem {
        TODO("Not yet implemented")
    }

    override fun registerInvalid(invalidItem: InvalidItem) {
        TODO("Not yet implemented")
    }

    override fun registerVolunteer(volunteerItem: VolunteerItem) {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(uuid: String, reason: String): Boolean {
        TODO("Not yet implemented")
    }
}