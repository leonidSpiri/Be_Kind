package ru.spiridonov.be.kind.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityRegisterBinding
import ru.spiridonov.be.kind.presentation.viewmodels.RegisterViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerType by lazy {
        parseIntent()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: RegisterViewModel

    private val component by lazy {
        (application as BeKindApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
        listenerRegisterBtn()
    }

    private fun listenerRegisterBtn() =
        with(binding) {
            login.setOnClickListener {
                val email = email.text
                val password = password.text
                val surname = surname.text
                val name = name.text
                val lastName = lastname.text
                val personalNumber = personalNumber.text
                val birthday = birthday.text
                val city = city.text
            }
        }

    private fun parseIntent(): String {
        if (!intent.hasExtra(REGISTER_TYPE))
            finish()
        val buff = intent.getStringExtra(REGISTER_TYPE)
        if (buff != INVALID_TYPE || buff != VOLUNTEER_TYPE) {
            finish()
            throw RuntimeException("REGISTER_TYPE was not found. REGISTER_TYPE = $buff")
        }
        return buff
    }


    companion object {
        private const val REGISTER_TYPE = "register_type"
        private const val INVALID_TYPE = "invalid_type"
        private const val VOLUNTEER_TYPE = "volunteer_type"

        fun newIntent(context: Context, registerType: String): Intent {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.putExtra(REGISTER_TYPE, registerType)
            return intent
        }
    }
}