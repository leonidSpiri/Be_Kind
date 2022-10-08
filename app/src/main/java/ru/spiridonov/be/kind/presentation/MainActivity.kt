package ru.spiridonov.be.kind.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityMainBinding
import ru.spiridonov.be.kind.domain.usecases.account_item.IsUserLoggedInUseCase
import ru.spiridonov.be.kind.presentation.account.AccountActivity
import ru.spiridonov.be.kind.presentation.account.UserProfileActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase

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
            binding.btnUserProfile.visibility = View.VISIBLE
        } else {
            binding.btnUserProfile.visibility = View.GONE
            binding.btnLoginInvalid.visibility = View.VISIBLE
            binding.btnLoginVolunteer.visibility = View.VISIBLE
        }



        with(binding) {
            btnLoginInvalid.setOnClickListener {
                startActivity(AccountActivity.newIntentInvalid(this@MainActivity))
            }
            btnLoginVolunteer.setOnClickListener {
                startActivity(AccountActivity.newIntentVolunteer(this@MainActivity))
            }
            btnUserProfile.setOnClickListener {
                startActivity(UserProfileActivity.newIntent(this@MainActivity))
            }
        }
    }
}