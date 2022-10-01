package ru.spiridonov.be.kind.presentation.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.R
import ru.spiridonov.be.kind.databinding.ActivityAccountBinding
import ru.spiridonov.be.kind.presentation.account.ui.LoginFragment
import ru.spiridonov.be.kind.presentation.account.ui.RegisterFragment

class AccountActivity : AppCompatActivity(), RegisterFragment.OnEditingFinished,
    LoginFragment.OnEditingFinished {
    private val binding by lazy {
        ActivityAccountBinding.inflate(layoutInflater)
    }
    private var registerType = MODE_UNKNOWN
    private val component by lazy {
        (application as BeKindApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        if (savedInstanceState == null)
            launchRightMode()
    }

    private fun launchRightMode() {
        val fragment = when (registerType) {
            INVALID_TYPE -> LoginFragment.newInstanceInvalid()
            VOLUNTEER_TYPE -> LoginFragment.newInstanceVolunteer()
            else -> throw RuntimeException("Unknown screen mode $registerType")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(REGISTER_TYPE))
            throw RuntimeException("Param screen mode is absent")
        val buff = intent.getStringExtra(REGISTER_TYPE)
        if (buff != INVALID_TYPE && buff != VOLUNTEER_TYPE)
            throw RuntimeException("REGISTER_TYPE was not found. REGISTER_TYPE = $buff")
        registerType = buff
    }

    override fun onEditingFinished() {
        finish()
    }

    companion object {
        private const val REGISTER_TYPE = "register_type"
        private const val INVALID_TYPE = "invalid_type"
        private const val VOLUNTEER_TYPE = "volunteer_type"
        private const val MODE_UNKNOWN = ""

        fun newIntentInvalid(context: Context): Intent {
            val intent = Intent(context, AccountActivity::class.java)
            intent.putExtra(REGISTER_TYPE, INVALID_TYPE)
            return intent
        }

        fun newIntentVolunteer(context: Context): Intent {
            val intent = Intent(context, AccountActivity::class.java)
            intent.putExtra(REGISTER_TYPE, VOLUNTEER_TYPE)
            return intent
        }
    }
}