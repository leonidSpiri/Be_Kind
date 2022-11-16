package ru.spiridonov.be.kind.presentation.active_work

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        observeViewModel()
        finishWork()
        addTextChangeListeners()
    }


    private fun observeViewModel() =
        viewModel.workItem.observe(this) {
            if (it != null) {
                binding.workItem = it
                workItem = it
            }
        }

    private fun finishWork() =
        binding.btnEndWork.setOnClickListener {
            if (viewModel.endVolunteerWork(binding.etEndWork.text.toString(), workItem))
                AlertDialog.Builder(this)
                    .setTitle("Завершение работы")
                    .setMessage("Работа завершена")
                    .setPositiveButton("Ок") { _, _ ->
                        finish()
                    }
                    .show()
        }

    private fun addTextChangeListeners() =
        with(binding) {
            etEndWork.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputFinishCode()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
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