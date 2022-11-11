package ru.spiridonov.be.kind.presentation.create_work

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityVolunteerApproveHelpItemBinding
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class VolunteerApproveHelpItemActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVolunteerApproveHelpItemBinding.inflate(layoutInflater)
    }
    private val component by lazy {
        (application as BeKindApp).component
    }
    private lateinit var workItem: WorkItem

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HelpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[HelpViewModel::class.java]
        observeViewModel()
        btnApproveListener()
    }

    private fun observeViewModel() =
        viewModel.getWorkItem(
            intent.getStringExtra(HELP_ID) ?: throw IllegalArgumentException("HELP_ID is null")
        ) {
            binding.workItem = it
            workItem = it ?: throw IllegalArgumentException("WorkItem is null")
        }

    private fun btnApproveListener() =
        binding.btnApprove.setOnClickListener {
            viewModel.approveWork(workItem)
            Toast.makeText(this, "Заявка принята", Toast.LENGTH_SHORT).show()
            finish()
        }

    companion object {
        const val HELP_ID = "help_id"
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, VolunteerApproveHelpItemActivity::class.java)
            intent.putExtra(HELP_ID, id)
            return intent
        }
    }
}