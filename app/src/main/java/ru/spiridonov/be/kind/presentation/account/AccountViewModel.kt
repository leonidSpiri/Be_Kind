package ru.spiridonov.be.kind.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.usecases.account_item.LoginInvalidUseCase
import ru.spiridonov.be.kind.domain.usecases.account_item.LoginVolunteerUseCase
import ru.spiridonov.be.kind.domain.usecases.account_item.RegisterInvalidUseCase
import ru.spiridonov.be.kind.domain.usecases.account_item.RegisterVolunteerUseCase
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val registerInvalidUseCase: RegisterInvalidUseCase,
    private val registerVolunteerUseCase: RegisterVolunteerUseCase,
    private val loginInvalidUseCase: LoginInvalidUseCase,
    private val loginVolunteerUseCase: LoginVolunteerUseCase
) : ViewModel() {

    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail
    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword
    private val _errorInputSurname = MutableLiveData<Boolean>()
    val errorInputSurname: LiveData<Boolean>
        get() = _errorInputSurname
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName
    private val _errorInputLastName = MutableLiveData<Boolean>()
    val errorInputLastName: LiveData<Boolean>
        get() = _errorInputLastName
    private val _errorInputPersonalNumber = MutableLiveData<Boolean>()
    val errorInputPersonalNumber: LiveData<Boolean>
        get() = _errorInputPersonalNumber
    private val _errorInputBirthday = MutableLiveData<Boolean>()
    val errorInputBirthday: LiveData<Boolean>
        get() = _errorInputBirthday
    private val _errorInputCity = MutableLiveData<Boolean>()
    val errorInputCity: LiveData<Boolean>
        get() = _errorInputCity
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen
    private val _shouldCloseLoginScreen = MutableLiveData<String>()
    val shouldCloseLoginScreen: LiveData<String>
        get() = _shouldCloseLoginScreen

    fun registerAccount(accountItem: AccountItem) {
        if (validateInput(accountItem)) {
            viewModelScope.launch {
                if (accountItem.type == INVALID_TYPE)
                    registerInvalidUseCase(
                        AccountItem(
                            type = parseStroke(accountItem.type),
                            surName = parseStroke(accountItem.surName!!),
                            password = parseStroke(accountItem.password),
                            name = parseStroke(accountItem.name!!),
                            lastname = parseStroke(accountItem.lastname!!),
                            personalPhone = parseStroke(accountItem.personalPhone!!),
                            email = parseStroke(accountItem.email),
                            birthday = accountItem.birthday!!,
                            city = parseStroke(accountItem.city!!),
                            helpReason = listOf(),
                        )
                    )
                else
                    registerVolunteerUseCase(
                        AccountItem(
                            type = parseStroke(accountItem.type),
                            surName = parseStroke(accountItem.surName!!),
                            password = parseStroke(accountItem.password),
                            name = parseStroke(accountItem.name!!),
                            lastname = parseStroke(accountItem.lastname!!),
                            personalPhone = parseStroke(accountItem.personalPhone!!),
                            email = parseStroke(accountItem.email),
                            birthday = accountItem.birthday!!,
                            city = parseStroke(accountItem.city!!),
                            helpReason = listOf(),
                        )
                    )
                finishWork()
            }
        }
    }

    fun loginAccount(accountItem: AccountItem) {
        if (accountItem.email.isBlank()) {
            _errorInputEmail.value = true
            return
        }
        if (accountItem.password.isBlank() || accountItem.password.length < 6) {
            _errorInputPassword.value = true
            return
        }
        viewModelScope.launch {
            if (accountItem.type == INVALID_TYPE) {
                loginInvalidUseCase(
                    accountItem.email,
                    accountItem.password
                ) { isGood, name ->
                    if (isGood)
                        _shouldCloseLoginScreen.setValue("Здравствуйте, $name")
                    else
                        _shouldCloseLoginScreen.setValue(name)
                }
            } else
                loginVolunteerUseCase(
                    accountItem.email,
                    accountItem.password
                ) { isGood, name ->
                    if (isGood)
                        _shouldCloseLoginScreen.setValue("Здравствуйте, $name")
                    else
                        _shouldCloseLoginScreen.setValue(name)
                }
        }

    }

    private fun validateInput(accountItem: AccountItem): Boolean {
        var result = true
        with(accountItem) {
            if (email.isBlank()) {
                _errorInputEmail.value = true
                result = false
            }
            if (password.isBlank() || password.length < 6) {
                _errorInputPassword.value = true
                result = false
            }
            if (surName == null || surName.isBlank()) {
                _errorInputSurname.value = true
                result = false
            }
            if (name == null || name.isBlank()) {
                _errorInputName.value = true
                result = false
            }
            if (lastname == null || lastname.isBlank()) {
                _errorInputLastName.value = true
                result = false
            }
            if (personalPhone == null || personalPhone!!.isBlank()) {
                _errorInputPersonalNumber.value = true
                result = false
            }
            if (birthday == null) {
                _errorInputBirthday.value = true
                result = false
            }
            if (city == null || city!!.isBlank()) {
                _errorInputCity.value = true
                result = false
            }
        }
        return result
    }

    private fun parseStroke(input: String?) = input?.trim() ?: ""

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }

    fun resetErrorInputSurname() {
        _errorInputSurname.value = false
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputLastName() {
        _errorInputLastName.value = false
    }

    fun resetErrorInputPersonalNumber() {
        _errorInputPersonalNumber.value = false
    }

    fun resetErrorInputBirthday() {
        _errorInputBirthday.value = false
    }

    fun resetErrorInputCity() {
        _errorInputCity.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    companion object {
        private const val INVALID_TYPE = "invalid_type"
    }
}