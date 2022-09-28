package ru.spiridonov.be.kind.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityMainBinding
import ru.spiridonov.be.kind.domain.usecases.account_item.GetExistingInvalidAccountUseCase
import ru.spiridonov.be.kind.domain.usecases.account_item.GetExistingVolunteerAccountUseCase
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

    private val component by lazy {
        (application as BeKindApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("MainActivity", getExistingInvalidAccountUseCase().toString())
        Log.d("MainActivity", getExistingVolunteerAccountUseCase().toString())
        startActivity(AccountActivity.newIntentInvalid(this))
    }
}