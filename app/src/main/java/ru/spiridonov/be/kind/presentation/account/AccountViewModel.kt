package ru.spiridonov.be.kind.presentation.account

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.data.mapper.AccountItemMapper
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.usecases.account_item.*
import ru.spiridonov.be.kind.domain.usecases.invalid_item.EditInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.invalid_item.GetInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.volunteer_item.EditVolunteerItemUseCase
import ru.spiridonov.be.kind.domain.usecases.volunteer_item.GetVolunteerItemUseCase
import ru.spiridonov.be.kind.utils.AllUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val application: Application,
    private val registerInvalidUseCase: RegisterInvalidUseCase,
    private val registerVolunteerUseCase: RegisterVolunteerUseCase,
    private val loginInvalidUseCase: LoginInvalidUseCase,
    private val loginVolunteerUseCase: LoginVolunteerUseCase,
    private val getVolunteerItemUseCase: GetVolunteerItemUseCase,
    private val getInvalidItemUseCase: GetInvalidItemUseCase,
    private val editInvalidItemUseCase: EditInvalidItemUseCase,
    private val editVolunteerItemUseCase: EditVolunteerItemUseCase,
    private val accountItemMapper: AccountItemMapper,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val logoutUseCase: LogoutUseCase
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
    private val _errorInputGender = MutableLiveData<Boolean>()
    val errorInputGender: LiveData<Boolean>
        get() = _errorInputGender
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen
    private val _shouldCloseLoginScreen = MutableLiveData<String>()
    val shouldCloseLoginScreen: LiveData<String>
        get() = _shouldCloseLoginScreen

    val spinnerGenderSelected = MutableLiveData<String>()

    private var uuid = ""


    fun uploadPhoto(bitmapPhoto: Bitmap, type: String) =
        viewModelScope.launch {
            CoroutineScope(Dispatchers.Default).launch {
                val storage = FirebaseStorage.getInstance().reference
                getUserInfo { account ->
                    val photoRef =
                        storage.child("images/users/${account?.type}/${uuid}/$type-${getDateHour()}.jpg")
                    val baos = java.io.ByteArrayOutputStream()
                    bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = photoRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        Toast.makeText(application, "Ошибка загрузки фото", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnSuccessListener {
                        Toast.makeText(application, "Фото загружено", Toast.LENGTH_SHORT).show()
                        when (type) {
                            "FirstPassport", "SecondPassport" -> {
                                if (account?.type == INVALID_TYPE)
                                    getInvalidItemUseCase {
                                        CoroutineScope(Dispatchers.Default).launch {
                                            editInvalidItemUseCase(it!!.copy(isPassportConfirmed = false)) {

                                            }
                                        }
                                    }
                                else
                                    getVolunteerItemUseCase {
                                        CoroutineScope(Dispatchers.Default).launch {
                                            editVolunteerItemUseCase(it!!.copy(isPassportConfirmed = false)) {

                                            }
                                        }
                                    }
                            }
                            "Cert" -> {
                                if (account?.type == INVALID_TYPE)
                                    getInvalidItemUseCase {
                                        CoroutineScope(Dispatchers.Default).launch {
                                            editInvalidItemUseCase(
                                                it!!.copy(
                                                    isCertOfDisabilityConfirmed = false
                                                )
                                            ) {

                                            }
                                        }
                                    }
                                else
                                    getVolunteerItemUseCase {
                                        CoroutineScope(Dispatchers.Default).launch {
                                            editVolunteerItemUseCase(
                                                it!!.copy(
                                                    isCertOfMedicalEduConfirmed = false
                                                )
                                            ) {

                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }

    fun getUserInfo(callback: (AccountItem?) -> Unit) {
        getVolunteerItemUseCase.invoke { volunteerItem ->
            if (volunteerItem != null) {
                var accountItem = accountItemMapper.mapVolunteerItemToAccountItem(volunteerItem)
                accountItem =
                    accountItem.copy(surName = "${accountItem.surName} ${accountItem.name} ${accountItem.lastname}")
                uuid = volunteerItem.uuid
                callback(accountItem)
            }
        }
        getInvalidItemUseCase.invoke { invalidItem ->
            if (invalidItem != null) {
                var accountItem = accountItemMapper.mapInvalidItemToAccountItem(invalidItem)
                accountItem =
                    accountItem.copy(surName = "${accountItem.surName} ${accountItem.name} ${accountItem.lastname}")
                uuid = invalidItem.uuid
                callback(accountItem)
            }
        }
    }

    fun logout() {
        logoutUseCase()
    }

    fun deleteAccount(): Boolean {
        getInvalidItemUseCase.invoke { invalidItem ->
            invalidItem?.uuid?.let { uuid ->
                deleteAccountUseCase(
                    uuid,
                    null
                )
            }
        }
        getVolunteerItemUseCase.invoke { volunteerItem ->
            volunteerItem?.uuid?.let { uuid ->
                deleteAccountUseCase(
                    uuid, null
                )
            }
        }
        return true
    }

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
                            gender = parseStroke(spinnerGenderSelected.value),
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
                            gender = parseStroke(spinnerGenderSelected.value),
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
        if (spinnerGenderSelected.value.isNullOrEmpty()) {
            _errorInputGender.value = true
            result = false
        }
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
            if (personalPhone == null || personalPhone!!.isBlank() || !AllUtils().isItPhone(
                    personalPhone!!
                )
            ) {
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

    private fun getDateHour(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
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

    fun resetErrorInputGender() {
        _errorInputGender.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    companion object {
        private const val INVALID_TYPE = "invalid_type"
    }
}