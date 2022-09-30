package ru.spiridonov.be.kind.domain.repository

import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem

interface AccountRepository {

    fun loginInvalid(login: String, password: String): InvalidItem

    fun loginVolunteer(login: String, password: String): VolunteerItem

    fun registerInvalid(accountItem: AccountItem): Boolean

    fun registerVolunteer(accountItem: AccountItem): Boolean

    fun getExistingInvalidAccount(): InvalidItem?

    fun getExistingVolunteerAccount(): VolunteerItem?

    fun deleteAccount(uuid: String, reason: String): Boolean
}