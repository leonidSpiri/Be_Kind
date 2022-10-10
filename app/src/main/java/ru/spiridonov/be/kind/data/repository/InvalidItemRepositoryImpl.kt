package ru.spiridonov.be.kind.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.repository.InvalidItemRepository
import ru.spiridonov.be.kind.domain.usecases.account_item.IsUserVerifiedUseCase
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class InvalidItemRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val isUserVerifiedUseCase: IsUserVerifiedUseCase
) : InvalidItemRepository {
    private val auth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun editInvalidItem(invalidItem: InvalidItem) {
        sharedPref.setInvalidAccountInfo(invalidItem)
    }

    override fun getInvalidItem(): InvalidItem? {
       updateInfo{
       }

        val item = sharedPref.getInvalidAccountInfo()
        if (item.uuid.isEmpty()) return null
        return item
    }

    private fun updateInfo( callback: (Boolean) -> Unit){
        db.collection("users").document("UsersCollection").collection("invalids")
            .document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val invalidItem = document.toObject<InvalidItem>()
                    invalidItem?.let {
                        if (isUserVerifiedUseCase())
                            sharedPref.setInvalidAccountInfo(it.copy(isAccountConfirmed = true))
                        else
                            sharedPref.setInvalidAccountInfo(it.copy(isAccountConfirmed = false))
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