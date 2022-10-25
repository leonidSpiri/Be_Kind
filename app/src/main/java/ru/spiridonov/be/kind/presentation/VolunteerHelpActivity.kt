package ru.spiridonov.be.kind.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityVolunteerHelpBinding
import ru.spiridonov.be.kind.presentation.adapters.WorkItemAdapter
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
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getWorkList()
        // viewModel.getWorkItem("zius1gEt0seSg23c3d3289-490f-4b97-972f-dd188610") {}
        viewModel.workList.observe(this) {
            val workItemAdapter = WorkItemAdapter()
            workItemAdapter.submitList(it)
            binding.rvWorkList.adapter = workItemAdapter
        }
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, VolunteerHelpActivity::class.java)
    }
}