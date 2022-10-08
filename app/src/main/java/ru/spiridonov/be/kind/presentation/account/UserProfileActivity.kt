package ru.spiridonov.be.kind.presentation.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityUserProfileBinding
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class UserProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as BeKindApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getUserInfo()?.let {
            binding.accountItem = it
        }
    }
}