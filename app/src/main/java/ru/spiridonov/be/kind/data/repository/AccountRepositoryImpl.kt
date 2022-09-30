package ru.spiridonov.be.kind.data.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.spiridonov.be.kind.data.mapper.AccountItemMapper
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import ru.spiridonov.be.kind.utils.SharedPref
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val application: Application,
    private val sharedPref: SharedPref,
    private val accountItemMapper: AccountItemMapper,
) : AccountRepository {
    private val auth by lazy {
        Firebase.auth
    }

    override fun loginInvalid(login: String, password: String): InvalidItem {
        TODO("Not yet implemented")
    }

    override fun loginVolunteer(login: String, password: String): VolunteerItem {
        TODO("Not yet implemented")
    }

    override fun registerInvalid(accountItem: AccountItem): Boolean {
        auth.createUserWithEmailAndPassword(accountItem.email, accountItem.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AccountRepositoryImpl", "registerInvalid: success")
                    val user = auth.currentUser
                    if (user != null) {
                        user.sendEmailVerification()
                            .addOnCompleteListener { taskEmail ->
                                if (taskEmail.isSuccessful) {
                                    Toast.makeText(
                                        application,
                                        "На почту выслано подтверждение",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("AccountRepositoryImpl", "Email sent.")
                                }
                            }

                        sharedPref.setInvalidAccountInfo(
                            accountItemMapper.mapAccountItemToInvalidItem(
                                user.uid,
                                accountItem
                            )
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
                        user.sendEmailVerification()
                            .addOnCompleteListener { taskEmail ->
                                if (taskEmail.isSuccessful) {
                                    Toast.makeText(
                                        application,
                                        "На почту выслано подтверждение",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("AccountRepositoryImpl", "Email sent.")
                                }
                            }

                        sharedPref.setVolunteerAccountInfo(
                            accountItemMapper.mapAccountItemToVolunteerItem(
                                user.uid,
                                accountItem
                            )
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

    override fun deleteAccount(uuid: String, reason: String): Boolean {
        TODO("Not yet implemented")
    }
}