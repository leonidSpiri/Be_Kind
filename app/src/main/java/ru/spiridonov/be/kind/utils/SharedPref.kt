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
            photoUrl = getUnnamedSharedPref("be_kind_account_invalid", "photoUrl"),
            passportGeneralUrl = getUnnamedSharedPref(
                "be_kind_account_invalid",
                "passportGeneralUrl"
            ),
            passportRegistrationUrl = getUnnamedSharedPref(
                "be_kind_account_invalid",
                "passportRegistrationUrl"
            ),
            certOfDisabilityUrl = getUnnamedSharedPref(
                "be_kind_account_invalid",
                "certOfDisabilityUrl"
            ),
            isAccountConfirmed = context.getSharedPreferences(
                "be_kind_account_invalid",
                Context.MODE_PRIVATE
            )
                .getBoolean("isAccountConfirmed", false)
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
            setUnnamedSharedPref("be_kind_account_invalid", "birthday", birthday)
            setUnnamedSharedPref("be_kind_account_invalid", "city", city)
            address?.let { setUnnamedSharedPref("be_kind_account_invalid", "address", it) }
            photoUrl?.let { setUnnamedSharedPref("be_kind_account_invalid", "photoUrl", it) }
            passportGeneralUrl?.let {
                setUnnamedSharedPref("be_kind_account_invalid", "passportGeneralUrl", it)
            }
            passportRegistrationUrl?.let {
                setUnnamedSharedPref("be_kind_account_invalid", "passportRegistrationUrl", it)
            }
            certOfDisabilityUrl?.let {
                setUnnamedSharedPref("be_kind_account_invalid", "certOfDisabilityUrl", it)
            }
            context.getSharedPreferences("be_kind_account_invalid", Context.MODE_PRIVATE).edit()
                .putBoolean("isAccountConfirmed", isAccountConfirmed).apply()
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
            photoUrl = getUnnamedSharedPref("be_kind_account_volunteer", "photoUrl"),
            passportGeneralUrl = getUnnamedSharedPref(
                "be_kind_account_volunteer",
                "passportGeneralUrl"
            ),
            passportRegistrationUrl = getUnnamedSharedPref(
                "be_kind_account_volunteer",
                "passportRegistrationUrl"
            ),
            certOfMedicalEduUrl = getUnnamedSharedPref(
                "be_kind_account_volunteer",
                "certOfMedicalEduUrl"
            ),
            isAccountConfirmed = context.getSharedPreferences(
                "be_kind_account_volunteer",
                Context.MODE_PRIVATE
            )
                .getBoolean("isAccountConfirmed", false)
        )

    fun setVolunteerAccountInfo(volunteerItem: VolunteerItem) =
        volunteerItem.apply {
            setUnnamedSharedPref("be_kind_account_volunteer", "uuid", uuid)
            setUnnamedSharedPref("be_kind_account_volunteer", "surName", surName)
            setUnnamedSharedPref("be_kind_account_volunteer", "name", name)
            setUnnamedSharedPref("be_kind_account_volunteer", "lastname", lastname)
            setUnnamedSharedPref("be_kind_account_volunteer", "personalPhone", personalPhone)
            setUnnamedSharedPref("be_kind_account_volunteer", "email", email)
            setUnnamedSharedPref("be_kind_account_volunteer", "birthday", birthday)
            setUnnamedSharedPref("be_kind_account_volunteer", "city", city)
            photoUrl?.let { setUnnamedSharedPref("be_kind_account_volunteer", "photoUrl", it) }
            passportGeneralUrl?.let {
                setUnnamedSharedPref("be_kind_account_volunteer", "passportGeneralUrl", it)
            }
            passportRegistrationUrl?.let {
                setUnnamedSharedPref("be_kind_account_volunteer", "passportRegistrationUrl", it)
            }
            certOfMedicalEduUrl?.let {
                setUnnamedSharedPref("be_kind_account_volunteer", "certOfMedicalEduUrl", it)
            }
            context.getSharedPreferences("be_kind_account_volunteer", Context.MODE_PRIVATE).edit()
                .putBoolean("isAccountConfirmed", isAccountConfirmed).apply()
        }
}