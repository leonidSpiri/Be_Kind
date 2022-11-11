package ru.spiridonov.be.kind.presentation.active_work

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityActiveVolunteerBinding
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class ActiveVolunteerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityActiveVolunteerBinding.inflate(layoutInflater)
    }
    private val component by lazy {
        (application as BeKindApp).component
    }

    private lateinit var workItem: WorkItem

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ActiveHelpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[ActiveHelpViewModel::class.java]
        observeViewModel()
    }


    private fun observeViewModel() =
        viewModel.workItem.observe(this) {
            if (it != null) {
                binding.workItem = it
                workItem = it
            }
        }


    companion object {
        private const val HELP_ID = "help_id"
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, ActiveVolunteerActivity::class.java)
            intent.putExtra(HELP_ID, id)
            return intent
        }
    }
}