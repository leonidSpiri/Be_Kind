package ru.spiridonov.be.kind.presentation.active_work

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityActiveInvalidBinding
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class ActiveInvalidActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityActiveInvalidBinding.inflate(layoutInflater)
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
        endWork()
    }


    private fun observeViewModel() =
        viewModel.workItem.observe(this) {
            if (it != null) {
                binding.workItem = it
                workItem = it
                Log.d("ActiveInvalidActivity", "observeViewModel: ${it}")
                if (workItem.whoHelpId != null)
                    showVolunteer(workItem.volunteerPhone)
            }
        }

    private fun endWork() =
        binding.btnEndWork.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Завершение работы")
                .setMessage("Вы уверены что хотите завершить работу?\n" +
                        "Покажите данный код волонтеру:\n${workItem.doneCode}")
                .setPositiveButton("Да") { _, _ ->
                    viewModel.setFinishStatus(workItem)
                finish()
                }
                .setNegativeButton("Нет") { _, _ -> }
                .show()
        }

    private fun showVolunteer(phone: String?) {
        if (!phone.isNullOrEmpty())
            binding.txtVolunteerPhone.text = "Телефон волонтера: $phone"
    }

    companion object {
        private const val HELP_ID = "help_id"
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, ActiveInvalidActivity::class.java)
            intent.putExtra(HELP_ID, id)
            return intent
        }
    }
}