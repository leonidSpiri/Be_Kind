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
import ru.spiridonov.be.kind.presentation.active_work.ActiveInvalidActivity
import ru.spiridonov.be.kind.presentation.active_work.ActiveVolunteerActivity
import ru.spiridonov.be.kind.presentation.create_work.InvalidHelpActivity
import ru.spiridonov.be.kind.presentation.create_work.VolunteerHelpListActivity
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
    }

    override fun onResume() {
        super.onResume()
        createScreen()
    }

    private fun createScreen() {
        if (viewModel.isUserLoggedIn()) {
            binding.btnLoginInvalid.visibility = View.GONE
            binding.btnLoginVolunteer.visibility = View.GONE
            binding.btnActiveWork.visibility = View.GONE
            binding.btnHelp.visibility = View.GONE
            binding.btnUserProfile.visibility = View.VISIBLE
            viewModel.getWork()
            viewModel.getUserInfo { account ->
                if (account != null) {
                    this.account = account
                    viewModel.workItem.observe(this){workItem ->
                        binding.btnLoginInvalid.visibility = View.GONE
                        binding.btnLoginVolunteer.visibility = View.GONE
                        binding.btnActiveWork.visibility = View.GONE
                        binding.btnHelp.visibility = View.GONE
                        if (workItem != null) {
                            val workId = workItem.id
                            binding.btnActiveWork.visibility = View.VISIBLE
                            if (account.type == INVALID_TYPE) {
                                //binding.btnActiveWork.text = "Мне нужна помощь"
                                binding.btnActiveWork.setOnClickListener {
                                    startActivity(ActiveInvalidActivity.newIntent(this, workId))
                                }
                            }
                            if (account.type == VOLUNTEER_TYPE) {
                                //binding.btnActiveWork.text = "Я хочу помочь"
                                binding.btnActiveWork.setOnClickListener {
                                    startActivity(
                                        ActiveVolunteerActivity.newIntent(
                                            this,
                                            workId
                                        )
                                    )
                                }
                            }
                        } else {
                            binding.btnHelp.visibility = View.VISIBLE
                            if (account.type == INVALID_TYPE) {
                                binding.btnHelp.text = "Мне нужна помощь"
                                binding.btnHelp.setOnClickListener {
                                    startActivity(InvalidHelpActivity.newIntent(this))
                                }
                            }
                            if (account.type == VOLUNTEER_TYPE) {
                                binding.btnHelp.text = "Я хочу помочь"
                                binding.btnHelp.setOnClickListener {
                                    startActivity(VolunteerHelpListActivity.newIntent(this))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            binding.btnUserProfile.visibility = View.GONE
            binding.btnHelp.visibility = View.GONE
            binding.btnActiveWork.visibility = View.GONE
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