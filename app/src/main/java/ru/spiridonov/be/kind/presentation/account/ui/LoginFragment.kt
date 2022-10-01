package ru.spiridonov.be.kind.presentation.account.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.R
import ru.spiridonov.be.kind.databinding.FragmentLoginBinding
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.presentation.account.AccountViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentLoginBinding == null")
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
        parseParams()
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        buttonListener()
        addTextChangeListeners()
        observeViewModel()
    }

    private fun buttonListener() =
        with(binding) {
            login.setOnClickListener {
                viewModel?.loginAccount(
                    AccountItem(
                        type = registerType,
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                    )
                )
            }
            register.setOnClickListener {
                launchRegisterMode()
            }
        }

    private fun launchRegisterMode() {
        val fragment = when (registerType) {
            INVALID_TYPE -> RegisterFragment.newInstanceInvalid()
            VOLUNTEER_TYPE -> RegisterFragment.newInstanceVolunteer()
            else -> throw RuntimeException("Unknown screen mode $registerType")
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun observeViewModel() {
        viewModel.shouldCloseLoginScreen.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            onEditingFinishedListener.onEditingFinished()
        }
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
        }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(REGISTER_TYPE))
            throw RuntimeException("Param screen mode is absent")
        val buff = args.getString(REGISTER_TYPE)
        if (buff != INVALID_TYPE && buff != VOLUNTEER_TYPE)
            throw RuntimeException("REGISTER_TYPE was not found. REGISTER_TYPE = $buff")
        registerType = buff
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

        fun newInstanceInvalid(): LoginFragment =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(REGISTER_TYPE, INVALID_TYPE)
                }
            }

        fun newInstanceVolunteer(): LoginFragment =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(REGISTER_TYPE, VOLUNTEER_TYPE)
                }
            }
    }
}