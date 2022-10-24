package ru.spiridonov.be.kind.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.R
import ru.spiridonov.be.kind.databinding.ActivityInvalidHelpBinding
import ru.spiridonov.be.kind.presentation.viewmodels.HelpViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class InvalidHelpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityInvalidHelpBinding.inflate(layoutInflater)
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        addTextChangeListeners()
        setAdapter()
        observeViewModel()
        btnListener()
    }


    private fun observeViewModel() {
        viewModel.getInvalidUserInfo { user ->
            binding.etAddress.setText(user?.address.toString())
            binding.etPersonalNumber.setText(user?.personalPhone.toString())
        }
    }

    private fun btnListener() {
        binding.btnConfirm.setOnClickListener {
            viewModel.createInvalidWork(
                address = binding.etAddress.text.toString(),
                time = binding.etTime.text.toString(),
                description = binding.etDescription.text.toString(),
                phone = binding.etPersonalNumber.text.toString()
            )
            Toast.makeText(this, "Заявка отправлена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setAdapter() {
        val adapterHelpType = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            helpTypeArray
        )
        (binding.dropdownHelpType
                as? AutoCompleteTextView)?.setAdapter(adapterHelpType)
        binding.dropdownHelpType.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.spinnerTypeSelected.value = helpTypeArray[position]
            }

        val adapterGender = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            genderArray
        )
        (binding.dropdownVolunteerGender as? AutoCompleteTextView)?.setAdapter(adapterGender)
        binding.dropdownVolunteerGender.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.spinnerGenderSelected.value = genderArray[position]
            }
        val adapterAge = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            ageArray
        )
        (binding.dropdownVolunteerAge as? AutoCompleteTextView)?.setAdapter(adapterAge)
        binding.dropdownVolunteerAge.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.spinnerAgeSelected.value = ageArray[position]
            }
    }

    private fun addTextChangeListeners() =
        with(binding) {
            dropdownHelpType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputHelpType()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            dropdownVolunteerGender.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputVolGender()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            dropdownVolunteerAge.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputVolAge()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etAddress.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputAddress()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etTime.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputHelpTime()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etPersonalNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputInvalidPhone()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }



/*
- передвижение в городе (поднять по лестнице/перевести через дорогу и тп)
- прогулка недалеко от дома (вам одиноко и/или вы нуждаетесь в сопровождении)
- общение (да, такое тоже может быть)
- периодическая медицинская помощь (уколы/инъекции/массаж - разово или на постоянной основе - будет оговорено)
- помощь по хозяйству (убрать/помочь помыться/приготовить еду и тп - разово или на постоянной основе- будет оговорено)
- другое (впишите в этой строке -открытый ответ)
- тип помощи:
- пол волонтера
- возраст волонтера
- адрес помощи
- время помощи
- описание помощи
- телефон инвалида
+ статус работы (ожидание, взята в работу, выполнена, отменена)
+ id работы
+ id волонтера
+ id инвалида
+ время создания
 */

    companion object {
        private val helpTypeArray = arrayOf(
            "передвижение в городе", "прогулка недалеко от дома", "общение",
            "периодическая медицинская помощь", "помощь по хозяйству", "другое"
        )
        private val genderArray = arrayOf("Мужской", "Женский")
        private val ageArray = arrayOf("18-25", "26-35", "36-45", "46-55", "56-65", "66+")
        fun newIntent(context: Context) =
            Intent(context, InvalidHelpActivity::class.java)
    }
}