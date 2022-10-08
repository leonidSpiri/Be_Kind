package ru.spiridonov.be.kind.data.mapper

import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import javax.inject.Inject

class AccountItemMapper @Inject constructor() {

    fun mapAccountItemToInvalidItem(uuid: String, accountItem: AccountItem): InvalidItem {
        return InvalidItem(
            uuid = uuid,
            surName = accountItem.surName.toString(),
            name = accountItem.name.toString(),
            lastname = accountItem.lastname.toString(),
            personalPhone = accountItem.personalPhone.toString(),
            relativePhone = accountItem.relativePhone,
            email = accountItem.email,
            birthday = accountItem.birthday.toString(),
            city = accountItem.city.toString(),
            helpReason = accountItem.helpReason!!,
        )
    }

    fun mapAccountItemToVolunteerItem(uuid: String, accountItem: AccountItem): VolunteerItem {
        return VolunteerItem(
            uuid = uuid,
            surName = accountItem.surName.toString(),
            name = accountItem.name.toString(),
            lastname = accountItem.lastname.toString(),
            personalPhone = accountItem.personalPhone.toString(),
            email = accountItem.email,
            birthday = accountItem.birthday.toString(),
            city = accountItem.city.toString(),
            helpReason = accountItem.helpReason!!,
        )
    }
}