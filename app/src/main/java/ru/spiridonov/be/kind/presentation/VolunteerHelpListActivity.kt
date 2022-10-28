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

class VolunteerHelpListActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVolunteerHelpBinding.inflate(layoutInflater)
    }
    private val component by lazy {
        (application as BeKindApp).component
    }
    private val workItemAdapter by lazy {
        WorkItemAdapter()
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
        setupRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.getWorkList()
        viewModel.workList.observe(this) {
            workItemAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvWorkList.adapter = workItemAdapter
        workItemAdapter.onWorkItemClickListener = {
            startActivity(VolunteerApproveHelpItemActivity.newIntent(this, it.id))
        }
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, VolunteerHelpListActivity::class.java)
    }
}