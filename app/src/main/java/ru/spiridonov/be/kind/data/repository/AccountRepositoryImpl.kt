package ru.spiridonov.be.kind.data.repository

import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref
) : AccountRepository {
    override fun loginInvalid(login: String, password: String): InvalidItem {
        TODO("Not yet implemented")
    }

    override fun loginVolunteer(login: String, password: String): VolunteerItem {
        TODO("Not yet implemented")
    }

    override fun registerInvalid(invalidItem: InvalidItem) {
        //TODO("Send to server and update uuid")
        sharedPref.setInvalidAccountInfo(invalidItem)
    }

    override fun registerVolunteer(volunteerItem: VolunteerItem) {
        //TODO("Send to server and update uuid")
        sharedPref.setVolunteerAccountInfo(volunteerItem)
    }

    override fun getExistingInvalidAccount(): InvalidItem? {
        val item = sharedPref.getInvalidAccountInfo()
        //if (item.uuid.isEmpty()) return null
        return item
    }

    override fun getExistingVolunteerAccount(): VolunteerItem? {
        val item = sharedPref.getVolunteerAccountInfo()
        //if (item.uuid.isEmpty()) return null
        return item
    }

    override fun deleteAccount(uuid: String, reason: String): Boolean {
        TODO("Not yet implemented")
    }
}