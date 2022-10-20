package ru.spiridonov.be.kind.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityMainBinding
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.presentation.account.AccountActivity
import ru.spiridonov.be.kind.presentation.account.UserProfileActivity
import ru.spiridonov.be.kind.presentation.viewmodels.MainViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    private val component by lazy {
        (application as BeKindApp).component
    }
    private var account: AccountItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        if (viewModel.isUserLoggedIn()) {
            binding.btnLoginInvalid.visibility = View.GONE
            binding.btnLoginVolunteer.visibility = View.GONE
            binding.btnUserProfile.visibility = View.VISIBLE
            viewModel.getUserInfo { account ->
                if (account != null) {
                    this.account = account
                    binding.btnHelp.visibility = View.VISIBLE
                    if (account.type == INVALID_TYPE) {
                        binding.btnHelp.text = "Попросить помощь"
                        binding.btnHelp.setOnClickListener {
                            startActivity(InvalidHelpActivity.newIntent(this))
                        }
                    }
                    if (account.type == VOLUNTEER_TYPE) {
                        binding.btnHelp.text = "Предложить помощь"
                        binding.btnHelp.setOnClickListener {
                            startActivity(VolunteerHelpActivity.newIntent(this))
                        }
                    }
                }
            }
        } else {
            binding.btnUserProfile.visibility = View.GONE
            binding.btnHelp.visibility = View.GONE
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

    companion object {
        private const val INVALID_TYPE = "invalid_type"
        private const val VOLUNTEER_TYPE = "volunteer_type"
    }
}