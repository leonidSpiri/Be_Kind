package ru.spiridonov.be.kind.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    override suspend fun editInvalidItem(
        invalidItem: InvalidItem,
        callback: (InvalidItem?) -> Unit
    ) {
        sharedPref.setInvalidAccountInfo(invalidItem)
        db.collection("users").document("UsersCollection")
            .collection("invalids")
            .document(invalidItem.uuid).set(invalidItem)
            .addOnCompleteListener {
                callback.invoke(invalidItem)
            }
    }

    override fun getInvalidItem(callback: (InvalidItem?) -> Unit) {
        db.collection("users").document("UsersCollection").collection("invalids")
            .document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val invalidItem = document.toObject<InvalidItem>()
                    invalidItem?.let {
                        if (isUserVerifiedUseCase()) {
                            sharedPref.setInvalidAccountInfo(it.copy(isAccountConfirmed = true))
                            if (!invalidItem.isAccountConfirmed) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    editInvalidItem(invalidItem.copy(isAccountConfirmed = true)) {}
                                }
                            }
                        } else
                            sharedPref.setInvalidAccountInfo(it.copy(isAccountConfirmed = false))

                        val item = sharedPref.getInvalidAccountInfo()
                        if (item.uuid.isEmpty()) callback(null)
                        callback.invoke(item)
                    }
                }
            }
            .addOnFailureListener {
                val item = sharedPref.getInvalidAccountInfo()
                if (item.uuid.isEmpty()) callback(null)
                callback.invoke(item)
            }
    }
}