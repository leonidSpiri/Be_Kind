package ru.spiridonov.be.kind.presentation.account.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.R
import ru.spiridonov.be.kind.databinding.FragmentRegisterBinding
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.presentation.account.AccountViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw RuntimeException("FragmentRegisterBinding == null")
    private var registerType = ""
    private lateinit var onEditingFinishedListener: OnEditingFinished
    private val component by lazy {
        (requireActivity().application as BeKindApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerType = parseParams()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinished) onEditingFinishedListener = context
        else throw RuntimeException("Activity must implement listener onEditingFinishedListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        listenerRegisterBtn()
        observeViewModel()
        setAdapter()
    }

    private fun setAdapter() {
        val genderArray = arrayOf("Мужской", "Женский")
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.dropdown_menu_popup_item,
            genderArray
        )
        (binding.dropdownGender as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.dropdownGender.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.spinnerGenderSelected.value = genderArray[position]
            }
    }

    private fun listenerRegisterBtn() =
        with(binding) {
            login.setOnClickListener {
                viewModel?.registerAccount(
                    AccountItem(
                        type = registerType,
                        surName = etSurname.text.toString(),
                        name = etName.text.toString(),
                        lastname = etLastname.text.toString(),
                        personalPhone = etPersonalNumber.text.toString(),
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                        birthday = etBirthday.text.toString(),
                        city = etCity.text.toString(),
                        relativePhone = null,
                        helpReason = null
                    )
                )
            }
        }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun parseParams(): String {
        val args = requireArguments()
        if (!args.containsKey(REGISTER_TYPE))
            throw RuntimeException("Param screen mode is absent")
        val buff = args.getString(REGISTER_TYPE)
        if (buff != INVALID_TYPE && buff != VOLUNTEER_TYPE)
            throw RuntimeException("REGISTER_TYPE was not found. REGISTER_TYPE = $buff")
        return buff
    }

    private fun addTextChangeListeners() =
        with(binding) {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputEmail()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputPassword()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etSurname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputSurname()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputName()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etLastname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputLastName()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etPersonalNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputPersonalNumber()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etBirthday.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputBirthday()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etCity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputCity()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            dropdownGender.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel?.resetErrorInputGender()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }

    interface OnEditingFinished {
        fun onEditingFinished()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val REGISTER_TYPE = "register_type"
        private const val INVALID_TYPE = "invalid_type"
        private const val VOLUNTEER_TYPE = "volunteer_type"

        fun newInstanceInvalid(): RegisterFragment =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(REGISTER_TYPE, INVALID_TYPE)
                }
            }

        fun newInstanceVolunteer(): RegisterFragment =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(REGISTER_TYPE, VOLUNTEER_TYPE)
                }
            }
    }
}