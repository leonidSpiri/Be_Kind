package ru.spiridonov.be.kind.presentation.account.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.FragmentDocumentsBinding
import ru.spiridonov.be.kind.presentation.account.AccountViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class DocumentsFragment : Fragment() {
    private var _binding: FragmentDocumentsBinding? = null
    private val binding: FragmentDocumentsBinding
        get() = _binding ?: throw RuntimeException("FragmentDocumentsBinding == null")
    private var registerType = ""
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
    }

    private fun parseParams() {
        arguments?.let {
            registerType = it.getString(REGISTER_TYPE) ?: ""
        }
    }

    companion object {
        private const val REGISTER_TYPE = "register_type"
        private const val INVALID_TYPE = "invalid_type"
        private const val VOLUNTEER_TYPE = "volunteer_type"

        fun newInstanceInvalid(): DocumentsFragment =
            DocumentsFragment().apply {
                arguments = Bundle().apply {
                    putString(REGISTER_TYPE, INVALID_TYPE)
                }
            }

        fun newInstanceVolunteer(): DocumentsFragment =
            DocumentsFragment().apply {
                arguments = Bundle().apply {
                    putString(REGISTER_TYPE, VOLUNTEER_TYPE)
                }
            }
    }
}