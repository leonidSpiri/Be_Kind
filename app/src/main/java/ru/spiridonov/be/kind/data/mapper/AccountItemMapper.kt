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

    fun mapInvalidItemToAccountItem(invalidItem: InvalidItem) = AccountItem(
        type = "invalid",
        surName = invalidItem.surName,
        password = "",
        name = invalidItem.name,
        lastname = invalidItem.lastname,
        personalPhone = invalidItem.personalPhone,
        relativePhone = invalidItem.relativePhone,
        email = invalidItem.email,
        birthday = invalidItem.birthday,
        city = invalidItem.city,
        helpReason = invalidItem.helpReason,
    )


    fun mapVolunteerItemToAccountItem(volunteerItem: VolunteerItem) = AccountItem(
        type = "volunteer",
        surName = volunteerItem.surName,
        password = "",
        name = volunteerItem.name,
        lastname = volunteerItem.lastname,
        personalPhone = volunteerItem.personalPhone,
        relativePhone = "",
        email = volunteerItem.email,
        birthday = volunteerItem.birthday,
        city = volunteerItem.city,
        helpReason = volunteerItem.helpReason,
    )
}