package ru.spiridonov.be.kind.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityVolunteerActiveHelpItemBinding
import ru.spiridonov.be.kind.presentation.viewmodels.HelpViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class VolunteerActiveHelpItemActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVolunteerActiveHelpItemBinding.inflate(layoutInflater)
    }
    private val component by lazy {
        (application as BeKindApp).component
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HelpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[HelpViewModel::class.java]
        viewModel.getWorkItem(
            intent.getStringExtra(HELP_ID) ?: throw IllegalArgumentException("HELP_ID is null")
        ) {
            Log.d("TAG", "onCreate: ${it.toString()}")
        }

    }

    companion object {
        const val HELP_ID = "help_id"
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, VolunteerActiveHelpItemActivity::class.java)
            intent.putExtra(HELP_ID, id)
            return intent
        }
    }
}