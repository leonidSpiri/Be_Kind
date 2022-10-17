package ru.spiridonov.be.kind.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import ru.spiridonov.be.kind.data.mapper.AccountItemMapper
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val accountItemMapper: AccountItemMapper,
) : AccountRepository {
    private val auth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        Firebase.firestore
    }

    override fun loginInvalid(
        login: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                db.collection("users").document("UsersCollection").collection("invalids")
                    .document(auth.currentUser!!.uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val invalidItem = document.toObject<InvalidItem>()
                            invalidItem?.let {
                                if (isUserVerified())
                                    sharedPref.setInvalidAccountInfo(it.copy(isAccountConfirmed = true))
                                else
                                    sharedPref.setInvalidAccountInfo(it.copy(isAccountConfirmed = false))

                                callback.invoke(true, it.name)
                            }
                        } else {
                            logout()
                            callback.invoke(false, "Информация о пользователе не найдена")
                        }
                    }
                    .addOnFailureListener {
                        logout()
                        callback.invoke(false, "Ошибка получения информации о пользователе")
                    }


            } else
                callback.invoke(
                    false, "Ошибка получения информации о пользователе\n" +
                            "Пользователь не найден"
                )
        }
    }

    override fun loginVolunteer(
        login: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                db.collection("users").document("UsersCollection")
                    .collection("volunteers")
                    .document(auth.currentUser!!.uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.data != null) {
                            val volunteerItem = document.toObject<VolunteerItem>()
                            volunteerItem?.let {
                                if (isUserVerified())
                                    sharedPref.setVolunteerAccountInfo(it.copy(isAccountConfirmed = true))
                                else
                                    sharedPref.setVolunteerAccountInfo(it.copy(isAccountConfirmed = false))
                                callback.invoke(true, it.name)
                            }
                        } else {
                            logout()
                            callback.invoke(false, "Информация о пользователе не найдена")
                        }
                    }
                    .addOnFailureListener {
                        logout()
                        callback.invoke(
                            false, "Ошибка получения информации о пользователе\n" +
                                    "\${exception.message}"
                        )
                    }
            } else
                callback.invoke(
                    false, "Ошибка получения информации о пользователе\n" +
                            "Пользователь не найден"
                )
        }
    }

    override fun registerInvalid(accountItem: AccountItem): Boolean {
        auth.createUserWithEmailAndPassword(accountItem.email, accountItem.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        sendEmailVerification()
                        createDatabaseInfoUser(
                            accountItemMapper.mapAccountItemToInvalidItem(
                                user.uid,
                                accountItem
                            ), null
                        )
                    }
                } else {
                    throw RuntimeException(
                        "registerInvalid: failure" +
                                " ${task.exception?.message}"
                    )
                }
            }
        return true
    }

    override fun registerVolunteer(accountItem: AccountItem): Boolean {
        auth.createUserWithEmailAndPassword(accountItem.email, accountItem.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        sendEmailVerification()
                        createDatabaseInfoUser(
                            null,
                            accountItemMapper.mapAccountItemToVolunteerItem(user.uid, accountItem)
                        )
                    }
                } else {
                    throw RuntimeException(
                        "registerVolunteer: failure" +
                                " ${task.exception?.message}"
                    )
                }
            }
        return true
    }

    override fun createDatabaseInfoUser(
        invalidItem: InvalidItem?,
        volunteerItem: VolunteerItem?
    ) {
        if (invalidItem != null && volunteerItem == null) {
            sharedPref.setInvalidAccountInfo(invalidItem)
            db.collection("users").document("UsersCollection")
                .collection("invalids")
                .document(invalidItem.uuid).set(invalidItem)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        auth.currentUser?.let { uuid -> deleteAccount(uuid.uid, null) }
                        throw RuntimeException(
                            "createDatabaseInfoUser: failure" +
                                    " ${it.exception?.message}"
                        )
                    }
                }

        } else if (volunteerItem != null && invalidItem == null) {
            sharedPref.setVolunteerAccountInfo(volunteerItem)
            db.collection("users").document("UsersCollection")
                .collection("volunteers")
                .document(volunteerItem.uuid).set(volunteerItem)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        auth.currentUser?.let { uuid -> deleteAccount(uuid.uid, null) }
                        throw RuntimeException(
                            "createDatabaseInfoUser: failure" +
                                    " ${it.exception?.message}"
                        )
                    }
                }
        }
    }

    override fun deleteAccount(uuid: String, reason: String?): Boolean {
        with(db.collection("users").document("UsersCollection")) {
            collection("invalids")
                .document(auth.currentUser?.uid!!).delete()
                .addOnCompleteListener {
                    auth.currentUser?.delete()
                }
                .addOnFailureListener {
                    throw RuntimeException(
                        "deleteAccount: failure" +
                                " ${it.message}"
                    )
                }
            collection("volunteers")
                .document(auth.currentUser?.uid!!).delete()
                .addOnCompleteListener {
                    auth.currentUser?.delete()
                }
                .addOnFailureListener {
                    throw RuntimeException(
                        "deleteAccount: failure" +
                                " ${it.message}"
                    )
                }
        }
        return true
    }

    override fun logout() {
        try {
            auth.signOut()
            sharedPref.deleteInvalidAccountInfo()
            sharedPref.deleteVolunteerAccountInfo()
        } catch (e: Exception) {
            Log.d("AccountRepositoryImpl", "logout: ${e.message}")
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return try {
            val currentUser = auth.currentUser
            currentUser != null
        } catch (e: Exception) {
            Log.d("AccountRepositoryImpl", "isUserLoggedIn: ${e.message}")
            logout()
            false
        }
    }

    override fun isUserVerified(): Boolean {
        return try {
            val currentUser = auth.currentUser
            currentUser != null && currentUser.isEmailVerified
        } catch (e: Exception) {
            Log.d("AccountRepositoryImpl", "isUserVerified: ${e.message}")
            false
        }
    }

    override fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { taskEmail ->
            if (taskEmail.isSuccessful) {
                Log.d("AccountRepositoryImpl", "Email sent.")
            } else
                Log.d("AccountRepositoryImpl", "Email not sent. ${taskEmail.exception?.message}")
        }
    }
}