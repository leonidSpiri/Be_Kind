package ru.spiridonov.be.kind.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.VolunteerItemRepository
import ru.spiridonov.be.kind.domain.usecases.account_item.IsUserVerifiedUseCase
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class VolunteerItemRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,

    private val isUserVerifiedUseCase: IsUserVerifiedUseCase
) : VolunteerItemRepository {

    private val auth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun editVolunteerItem(VolunteerItem: VolunteerItem) {
        sharedPref.setVolunteerAccountInfo(VolunteerItem)
    }

    override fun getVolunteerItem(): VolunteerItem? {
        updateInfo{
        }
        val item = sharedPref.getVolunteerAccountInfo()
        if (item.uuid.isEmpty()) return null
        return item
    }

    private fun updateInfo(callback: (Boolean) -> Unit) {
        db.collection("users").document("UsersCollection").collection("volunteers")
            .document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val volunteerItem = document.toObject<VolunteerItem>()
                    volunteerItem?.let {
                        if (isUserVerifiedUseCase())
                            sharedPref.setVolunteerAccountInfo(it.copy(isAccountConfirmed = true))
                        else
                            sharedPref.setVolunteerAccountInfo(it.copy(isAccountConfirmed = false))
                        callback.invoke(true)
                    }
                } else {
                    callback.invoke(false)
                }
            }
            .addOnFailureListener {
                callback.invoke(false)
            }
    }
}