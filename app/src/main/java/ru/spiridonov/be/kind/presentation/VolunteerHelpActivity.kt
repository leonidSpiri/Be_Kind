package ru.spiridonov.be.kind.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityVolunteerHelpBinding
import ru.spiridonov.be.kind.presentation.viewmodels.HelpViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class VolunteerHelpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVolunteerHelpBinding.inflate(layoutInflater)
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
    }


    companion object {
        fun newIntent(context: Context) =
            Intent(context, VolunteerHelpActivity::class.java)
    }
}