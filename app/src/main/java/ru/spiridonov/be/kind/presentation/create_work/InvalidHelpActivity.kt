package ru.spiridonov.be.kind.presentation.create_work

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.R
import ru.spiridonov.be.kind.databinding.ActivityInvalidHelpBinding
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import ru.spiridonov.be.kind.utils.AllUtils
import java.util.*
import javax.inject.Inject

class InvalidHelpActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val binding by lazy {
        ActivityInvalidHelpBinding.inflate(layoutInflater)
    }
    private val component by lazy {
        (application as BeKindApp).component
    }
    private var day = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    private var myDay = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0
    private var myHour: Int = 0
    private var myMinute: Int = 0

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
                description = binding.etDescription.text.toString(),
                phone = binding.etPersonalNumber.text.toString()
            )
            Toast.makeText(this, "???????????? ????????????????????", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnGetDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(
                    this@InvalidHelpActivity,
                    this@InvalidHelpActivity,
                    year,
                    month,
                    day
                )
            datePickerDialog.show()
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


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this@InvalidHelpActivity, this@InvalidHelpActivity, hour, minute,
            is24HourFormat(this@InvalidHelpActivity)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        binding.btnGetDate.text =
            String.format(dateTimeString, myDay, (myMonth + 1), myYear, myHour, myMinute)
        val longDate = AllUtils().stringToDateLong(
            "${myDay}.${myMonth + 1}.${myYear} ${myHour}:${myMinute}",
        )
        viewModel.selectedDate.value = if (longDate == -1L) null else longDate

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
- ???????????????????????? ?? ???????????? (?????????????? ???? ????????????????/?????????????????? ?????????? ???????????? ?? ????)
- ???????????????? ???????????????? ???? ???????? (?????? ?????????????? ??/?????? ???? ???????????????????? ?? ??????????????????????????)
- ?????????????? (????, ?????????? ???????? ?????????? ????????)
- ?????????????????????????? ?????????????????????? ???????????? (??????????/????????????????/???????????? - ???????????? ?????? ???? ???????????????????? ???????????? - ?????????? ??????????????????)
- ???????????? ???? ?????????????????? (????????????/???????????? ????????????????/?????????????????????? ?????? ?? ???? - ???????????? ?????? ???? ???????????????????? ????????????- ?????????? ??????????????????)
- ???????????? (?????????????? ?? ???????? ???????????? -???????????????? ??????????)
- ?????? ????????????:
- ?????? ??????????????????
- ?????????????? ??????????????????
- ?????????? ????????????
- ?????????? ????????????
- ???????????????? ????????????
- ?????????????? ????????????????
+ ???????????? ???????????? (????????????????, ?????????? ?? ????????????, ??????????????????, ????????????????)
+ id ????????????
+ id ??????????????????
+ id ????????????????
+ ?????????? ????????????????
 */

    companion object {
        private const val dateTimeString = "%d.%d.%d %d:%d"
        private val helpTypeArray = arrayOf(
            "???????????????????????? ?? ????????????", "???????????????? ???????????????? ???? ????????", "??????????????",
            "?????????????????????????? ?????????????????????? ????????????", "???????????? ???? ??????????????????", "????????????"
        )
        private val genderArray = arrayOf("??????????????", "??????????????")
        private val ageArray = arrayOf("18-25", "26-35", "36-45", "46-55", "56-65", "66+")
        fun newIntent(context: Context) =
            Intent(context, InvalidHelpActivity::class.java)
    }
}