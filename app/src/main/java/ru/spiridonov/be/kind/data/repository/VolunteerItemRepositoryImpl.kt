package ru.spiridonov.be.kind.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    override suspend fun editVolunteerItem(
        volunteerItem: VolunteerItem,
        callback: (VolunteerItem?) -> Unit
    ) {
        sharedPref.setVolunteerAccountInfo(volunteerItem)
        db.collection("users").document("UsersCollection")
            .collection("volunteers")
            .document(volunteerItem.uuid).set(volunteerItem)
            .addOnCompleteListener {
                callback.invoke(volunteerItem)
            }
    }

    override fun getVolunteerItem(callback: (VolunteerItem?) -> Unit) {
        db.collection("users").document("UsersCollection").collection("volunteers")
            .document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val volunteerItem = document.toObject<VolunteerItem>()
                    volunteerItem?.let {
                        callback.invoke(it)
                        if (isUserVerifiedUseCase()) {
                            sharedPref.setVolunteerAccountInfo(it.copy(isAccountConfirmed = true))
                            if (!volunteerItem.isAccountConfirmed) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    editVolunteerItem(volunteerItem.copy(isAccountConfirmed = true)) {}
                                }
                            }
                        } else
                            sharedPref.setVolunteerAccountInfo(it.copy(isAccountConfirmed = false))
                        val item = sharedPref.getVolunteerAccountInfo()
                        if (item.uuid.isEmpty()) callback(null)
                        else callback.invoke(item)
                    }
                } else callback.invoke(null)
            }
            .addOnFailureListener {
                val item = sharedPref.getVolunteerAccountInfo()
                if (item.uuid.isEmpty()) callback(null)
                callback.invoke(item)
            }
    }
}