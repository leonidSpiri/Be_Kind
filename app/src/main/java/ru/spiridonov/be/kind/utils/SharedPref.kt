package ru.spiridonov.be.kind.utils

import android.content.Context
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import javax.inject.Inject

class SharedPref @Inject constructor() {
    private val context = BeKindApp.appContext

    private fun getUnnamedSharedPref(name: String, key: String): String? =
        context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, "")

    private fun setUnnamedSharedPref(name: String, key: String, value: String) =
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value)
            .apply()

    fun getSharedPref(key: String): String = getUnnamedSharedPref("be_kind", key) ?: ""

    fun setSharedPref(key: String, value: String) =
        setUnnamedSharedPref("be_kind", key, value)

    fun getLongSharedPref(key: String) =
        context.getSharedPreferences("be_kind", Context.MODE_PRIVATE).getLong(key, -1L)

    fun setLongSharedPref(key: String, value: Long) =
        context.getSharedPreferences("be_kind", Context.MODE_PRIVATE).edit().putLong(key, value)
            .apply()

    fun getInvalidAccountInfo() =
        InvalidItem(
            uuid = getUnnamedSharedPref("be_kind_account_invalid", "uuid") ?: "",
            surName = getUnnamedSharedPref("be_kind_account_invalid", "surName") ?: "",
            name = getUnnamedSharedPref("be_kind_account_invalid", "name") ?: "",
            lastname = getUnnamedSharedPref("be_kind_account_invalid", "lastname") ?: "",
            personalPhone = getUnnamedSharedPref("be_kind_account_invalid", "personalPhone") ?: "",
            relativePhone = getUnnamedSharedPref("be_kind_account_invalid", "relativePhone"),
            email = getUnnamedSharedPref("be_kind_account_invalid", "email") ?: "",
            birthday = getUnnamedSharedPref("be_kind_account_invalid", "birthday") ?: "",
            city = getUnnamedSharedPref("be_kind_account_invalid", "city") ?: "",
            address = getUnnamedSharedPref("be_kind_account_invalid", "address"),
            helpReason = listOf(),
            gender = getUnnamedSharedPref("be_kind_account_invalid", "gender") ?: "",
            userRating = getUnnamedSharedPref(
                "be_kind_account_invalid",
                "userRating"
            )?.toIntOrNull()
                ?: 0,
            photoUrl = getUnnamedSharedPref("be_kind_account_invalid", "photoUrl"),
            isAccountConfirmed = context.getSharedPreferences(
                "be_kind_account_invalid",
                Context.MODE_PRIVATE
            )
                .getBoolean("isAccountConfirmed", false),
            isPassportConfirmed = context.getSharedPreferences(
                "be_kind_account_invalid",
                Context.MODE_PRIVATE
            )
                .getBoolean("isPassportConfirmed", false),
            isCertOfDisabilityConfirmed = context.getSharedPreferences(
                "be_kind_account_invalid",
                Context.MODE_PRIVATE
            )
                .getBoolean("isCertOfDisabilityConfirmed", false),
        )

    fun setInvalidAccountInfo(invalidItem: InvalidItem) =
        invalidItem.apply {
            setUnnamedSharedPref("be_kind_account_invalid", "uuid", uuid)
            setUnnamedSharedPref("be_kind_account_invalid", "surName", surName)
            setUnnamedSharedPref("be_kind_account_invalid", "name", name)
            setUnnamedSharedPref("be_kind_account_invalid", "lastname", lastname)
            setUnnamedSharedPref("be_kind_account_invalid", "personalPhone", personalPhone)
            relativePhone?.let {
                setUnnamedSharedPref("be_kind_account_invalid", "relativePhone", it)
            }
            setUnnamedSharedPref("be_kind_account_invalid", "email", email)
            setUnnamedSharedPref("be_kind_account_invalid", "birthday", birthday ?: "")
            setUnnamedSharedPref("be_kind_account_invalid", "gender", gender)
            setUnnamedSharedPref("be_kind_account_invalid", "userRating", userRating.toString())
            address?.let { setUnnamedSharedPref("be_kind_account_invalid", "address", it) }
            photoUrl?.let { setUnnamedSharedPref("be_kind_account_invalid", "photoUrl", it) }
            context.getSharedPreferences("be_kind_account_invalid", Context.MODE_PRIVATE).edit()
                .putBoolean("isAccountConfirmed", isAccountConfirmed).apply()
            context.getSharedPreferences("be_kind_account_invalid", Context.MODE_PRIVATE).edit()
                .putBoolean("isPassportConfirmed", isPassportConfirmed).apply()
            context.getSharedPreferences("be_kind_account_invalid", Context.MODE_PRIVATE).edit()
                .putBoolean("isCertOfDisabilityConfirmed", isCertOfDisabilityConfirmed).apply()
        }

    fun getVolunteerAccountInfo() =
        VolunteerItem(
            uuid = getUnnamedSharedPref("be_kind_account_volunteer", "uuid") ?: "",
            surName = getUnnamedSharedPref("be_kind_account_volunteer", "surName") ?: "",
            name = getUnnamedSharedPref("be_kind_account_volunteer", "name") ?: "",
            lastname = getUnnamedSharedPref("be_kind_account_volunteer", "lastname") ?: "",
            personalPhone = getUnnamedSharedPref("be_kind_account_volunteer", "personalPhone")
                ?: "",
            email = getUnnamedSharedPref("be_kind_account_volunteer", "email") ?: "",
            birthday = getUnnamedSharedPref("be_kind_account_volunteer", "birthday") ?: "",
            city = getUnnamedSharedPref("be_kind_account_volunteer", "city") ?: "",
            helpReason = listOf(),
            gender = getUnnamedSharedPref("be_kind_account_volunteer", "gender") ?: "",
            userRating = getUnnamedSharedPref(
                "be_kind_account_invalid",
                "userRating"
            )?.toIntOrNull()
                ?: 0,
            endedHelp = getUnnamedSharedPref(
                "be_kind_account_volunteer",
                "endedHelp"
            )?.toIntOrNull()
                ?: 0,
            photoUrl = getUnnamedSharedPref("be_kind_account_volunteer", "photoUrl"),
            isAccountConfirmed = context.getSharedPreferences(
                "be_kind_account_volunteer",
                Context.MODE_PRIVATE
            )
                .getBoolean("isAccountConfirmed", false),
            isPassportConfirmed = context.getSharedPreferences(
                "be_kind_account_volunteer",
                Context.MODE_PRIVATE
            )
                .getBoolean("isPassportConfirmed", false),
            isCertOfMedicalEduConfirmed = context.getSharedPreferences(
                "be_kind_account_volunteer",
                Context.MODE_PRIVATE
            )
                .getBoolean("isCertOfMedicalEduConfirmed", false),
        )

    fun setVolunteerAccountInfo(volunteerItem: VolunteerItem) =
        volunteerItem.apply {
            setUnnamedSharedPref("be_kind_account_volunteer", "uuid", uuid)
            setUnnamedSharedPref("be_kind_account_volunteer", "surName", surName)
            setUnnamedSharedPref("be_kind_account_volunteer", "name", name)
            setUnnamedSharedPref("be_kind_account_volunteer", "lastname", lastname)
            setUnnamedSharedPref("be_kind_account_volunteer", "personalPhone", personalPhone)
            setUnnamedSharedPref("be_kind_account_volunteer", "email", email)
            setUnnamedSharedPref("be_kind_account_volunteer", "birthday", birthday ?: "")
            setUnnamedSharedPref("be_kind_account_volunteer", "city", city)
            setUnnamedSharedPref("be_kind_account_volunteer", "gender", gender)
            setUnnamedSharedPref("be_kind_account_volunteer", "userRating", userRating.toString())
            setUnnamedSharedPref("be_kind_account_volunteer", "endedHelp", endedHelp.toString())
            photoUrl?.let { setUnnamedSharedPref("be_kind_account_volunteer", "photoUrl", it) }
            context.getSharedPreferences("be_kind_account_volunteer", Context.MODE_PRIVATE).edit()
                .putBoolean("isAccountConfirmed", isAccountConfirmed).apply()
            context.getSharedPreferences("be_kind_account_volunteer", Context.MODE_PRIVATE).edit()
                .putBoolean("isPassportConfirmed", isPassportConfirmed).apply()
            context.getSharedPreferences("be_kind_account_volunteer", Context.MODE_PRIVATE).edit()
                .putBoolean("isCertOfMedicalEduConfirmed", isCertOfMedicalEduConfirmed).apply()
        }

    fun deleteInvalidAccountInfo() {
        context.getSharedPreferences("be_kind_account_invalid", Context.MODE_PRIVATE).edit()
            .clear().apply()
        context.getSharedPreferences("be_kind", Context.MODE_PRIVATE).edit()
            .clear().apply()
    }

    fun deleteVolunteerAccountInfo() {
        context.getSharedPreferences("be_kind_account_volunteer", Context.MODE_PRIVATE).edit()
            .clear().apply()
        context.getSharedPreferences("be_kind", Context.MODE_PRIVATE).edit()
            .clear().apply()
    }

}