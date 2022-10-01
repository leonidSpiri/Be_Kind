package ru.spiridonov.be.kind.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
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
        Log.d("AccountRepositoryImpl", "loginInvalid")
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                db.collection("users").document("UsersCollection").collection("invalids")
                    .document(auth.currentUser!!.uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(
                                "AccountRepositoryImpl",
                                "DocumentSnapshot data: ${document.data}"
                            )
                            val invalidItem = document.toObject<InvalidItem>()
                            invalidItem?.let {
                                sharedPref.setInvalidAccountInfo(it)
                                Log.d(
                                    "AccountRepositoryImpl",
                                    "DocumentSnapshot data: ${sharedPref.getInvalidAccountInfo()}"
                                )
                                callback.invoke(true, it.name)
                            }
                        } else {
                            Log.d("AccountRepositoryImpl", "No such document")
                            callback.invoke(false, "Информация о пользователе не найдена")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("AccountRepositoryImpl", "get failed with ", exception)
                        callback.invoke(false, "Ошибка получения информации о пользователе")
                    }


            } else {
                Log.w("AccountRepositoryImpl", "signInWithEmail:failure", task.exception)
                callback.invoke(false, "Ошибка авторизации")
            }
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
                            Log.d(
                                "AccountRepositoryImpl",
                                "DocumentSnapshot data: ${document.data}"
                            )
                            val volunteerItem = document.toObject<VolunteerItem>()
                            volunteerItem?.let {
                                sharedPref.setVolunteerAccountInfo(it)
                                callback.invoke(true, it.name)
                            }
                        } else {
                            Log.d("AccountRepositoryImpl", "No such document")
                            logout()
                            callback.invoke(false, "Информация о пользователе не найдена")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("AccountRepositoryImpl", "get failed with ", exception)
                        logout()
                        callback.invoke(
                            false, "Ошибка получения информации о пользователе\n" +
                                    "\${exception.message}"
                        )
                    }
            }
        }
    }

    override fun registerInvalid(accountItem: AccountItem): Boolean {
        auth.createUserWithEmailAndPassword(accountItem.email, accountItem.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AccountRepositoryImpl", "registerInvalid: success")
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
                    Log.d("AccountRepositoryImpl", "registerInvalid: failure")
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
                    Log.d("AccountRepositoryImpl", "registerVolunteer: success")
                    val user = auth.currentUser
                    if (user != null) {
                        sendEmailVerification()
                        createDatabaseInfoUser(
                            null,
                            accountItemMapper.mapAccountItemToVolunteerItem(user.uid, accountItem)
                        )
                    }
                } else {
                    Log.d("AccountRepositoryImpl", "registerVolunteer: failure")
                    throw RuntimeException(
                        "registerVolunteer: failure" +
                                " ${task.exception?.message}"
                    )
                }
            }
        return true
    }

    override fun getExistingInvalidAccount(): InvalidItem? {
        val item = sharedPref.getInvalidAccountInfo()
        val currentUser = auth.currentUser
        if (item.uuid.isEmpty() && currentUser != null) return null
        return item
    }

    override fun getExistingVolunteerAccount(): VolunteerItem? {
        val item = sharedPref.getVolunteerAccountInfo()
        val currentUser = auth.currentUser
        if (item.uuid.isEmpty() && currentUser != null) return null
        return item
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
        val userCollection = db.collection("users").document("UsersCollection")
        val completeListener = { it: Task<Void> ->
            if (!it.isSuccessful) {
                throw RuntimeException(
                    "deleteAccount: failure" +
                            " ${it.exception?.message}"
                )
            } else
                auth.currentUser?.delete()
        }
        userCollection.collection("invalids")
            .document(auth.currentUser?.uid!!).delete()
            .addOnCompleteListener {
                completeListener(it)
            }

        userCollection.collection("volunteers")
            .document(auth.currentUser?.uid!!).delete()
            .addOnCompleteListener {
                completeListener(it)
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
            false
        }
    }

    override fun isUserVerified(): Boolean {
        val user = auth.currentUser
        return user?.isEmailVerified ?: false
    }

    override fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { taskEmail ->
            if (taskEmail.isSuccessful) {
                Log.d("AccountRepositoryImpl", "Email sent.")
            }
            else
                Log.d("AccountRepositoryImpl", "Email not sent. ${taskEmail.exception?.message}")
        }
    }
}