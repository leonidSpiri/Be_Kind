package ru.spiridonov.be.kind.domain.repository

import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem

interface AccountRepository {

    fun loginInvalid(login: String, password: String, callback: (Boolean, String) -> Unit)

    fun loginVolunteer(login: String, password: String, callback: (Boolean, String) -> Unit)

    fun registerInvalid(accountItem: AccountItem): Boolean

    fun registerVolunteer(accountItem: AccountItem): Boolean

    fun deleteAccount(uuid: String, reason: String?): Boolean

    fun logout()

    fun isUserLoggedIn(): Boolean

    fun isUserVerified(): Boolean

    fun sendEmailVerification()

    fun createDatabaseInfoUser(invalidItem: InvalidItem?, volunteerItem: VolunteerItem?)

}