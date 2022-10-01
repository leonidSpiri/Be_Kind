package ru.spiridonov.be.kind.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityMainBinding
import ru.spiridonov.be.kind.domain.usecases.account_item.*
import ru.spiridonov.be.kind.presentation.account.AccountActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var getExistingInvalidAccountUseCase: GetExistingInvalidAccountUseCase

    @Inject
    lateinit var getExistingVolunteerAccountUseCase: GetExistingVolunteerAccountUseCase

    @Inject
    lateinit var deleteAccountUseCase: DeleteAccountUseCase

    @Inject
    lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase

    @Inject
    lateinit var isUserVerifiedUseCase: IsUserVerifiedUseCase

    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    @Inject
    lateinit var sendEmailVerificationUseCase: SendEmailVerificationUseCase

    private val component by lazy {
        (application as BeKindApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (isUserLoggedInUseCase()) {
            binding.btnLoginInvalid.visibility = View.GONE
            binding.btnLoginVolunteer.visibility = View.GONE
            if (!isUserVerifiedUseCase())
                Toast.makeText(this, "Подтвердите почту", Toast.LENGTH_SHORT).show()
            binding.textView.textSize = 20f
            binding.textView.text = if (getExistingInvalidAccountUseCase() != null)
                getExistingInvalidAccountUseCase().toString()
            else
                getExistingVolunteerAccountUseCase().toString()
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.btnSignOut.visibility = View.GONE
        }



        with(binding) {
            btnLoginInvalid.setOnClickListener {
                startActivity(AccountActivity.newIntentInvalid(this@MainActivity))
            }
            btnLoginVolunteer.setOnClickListener {
                startActivity(AccountActivity.newIntentVolunteer(this@MainActivity))
            }
            btnSignOut.setOnClickListener {
                logoutUseCase()
                recreate()
            }
            btnDelete.setOnClickListener {
                if (isUserVerifiedUseCase()) {
                    if (getExistingInvalidAccountUseCase()?.uuid?.let { uuid ->
                            deleteAccountUseCase(
                                uuid,
                                null
                            )
                        } == true) recreate()
                    else if (getExistingVolunteerAccountUseCase()?.uuid?.let { uuid ->
                            deleteAccountUseCase(
                                uuid, null
                            )
                        } == true) recreate()
                } else {
                    Toast.makeText(this@MainActivity, "Подтвердите почту", Toast.LENGTH_SHORT)
                        .show()
                    sendEmailVerificationUseCase()
                }
            }
        }
    }
}