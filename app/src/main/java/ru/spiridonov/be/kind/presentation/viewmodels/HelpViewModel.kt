package ru.spiridonov.be.kind.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.usecases.invalid_item.GetInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.EditWorkItemUseCase
import ru.spiridonov.be.kind.utils.AllUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HelpViewModel @Inject constructor(
    private val getInvalidItemUseCase: GetInvalidItemUseCase,
    private val editWorkItemUseCase: EditWorkItemUseCase
) : ViewModel() {

    private val _errorInputHelpType = MutableLiveData<Boolean>()
    val errorInputHelpType: LiveData<Boolean>
        get() = _errorInputHelpType
    private val _errorInputVolGender = MutableLiveData<Boolean>()
    val errorInputVolGender: LiveData<Boolean>
        get() = _errorInputVolGender
    private val _errorInputVolAge = MutableLiveData<Boolean>()
    val errorInputVolAge: LiveData<Boolean>
        get() = _errorInputVolAge
    private val _errorInputAddress = MutableLiveData<Boolean>()
    val errorInputAddress: LiveData<Boolean>
        get() = _errorInputAddress
    private val _errorInputHelpTime = MutableLiveData<Boolean>()
    val errorInputHelpTime: LiveData<Boolean>
        get() = _errorInputHelpTime
    private val _errorInputInvalidPhone = MutableLiveData<Boolean>()
    val errorInputInvalidPhone: LiveData<Boolean>
        get() = _errorInputInvalidPhone

    val spinnerTypeSelected = MutableLiveData<String>()
    val spinnerGenderSelected = MutableLiveData<String>()
    val spinnerAgeSelected = MutableLiveData<String>()

    private var uuid = ""

    fun getInvalidUserInfo(callback: (InvalidItem?) -> Unit) {
        getInvalidItemUseCase.invoke { invalidItem ->
            if (invalidItem != null) {
                uuid = invalidItem.uuid
                callback(invalidItem)
            }
        }
    }

    fun createInvalidWork(address: String, time: String, description: String, phone: String) {
        val idStr = (uuid.dropLast(uuid.length / 2) + (UUID.randomUUID().toString()
            .dropLast(4)))
        if (validateInput(idStr, address, time, description, phone)) {
            val whenNeedHelp = stringToDateLong(time)
            viewModelScope.launch {
                editWorkItemUseCase.invoke(
                    WorkItem(
                        id = idStr,
                        description = parseStroke(description),
                        whoNeedHelpId = uuid,
                        timestamp = System.currentTimeMillis(),
                        whenNeedHelp = whenNeedHelp,
                        kindOfHelp = spinnerTypeSelected.value!!,
                        invalidPhone = phone,
                        address = parseStroke(address),
                        volunteerAge = spinnerAgeSelected.value!!,
                        volunteerGender = spinnerGenderSelected.value!!
                    )
                )
            }
        }
    }

    private fun validateInput(
        id: String?,
        address: String,
        time: String,
        description: String,
        phone: String
    ): Boolean {
        var result = true
        if (id.isNullOrEmpty()) {
            _errorInputHelpType.postValue(true)
            result = false
        }
        if (address.isEmpty()) {
            _errorInputAddress.value = true
            result = false
        }
        if (time.isEmpty() || stringToDateLong(time) == -1L) {
            _errorInputHelpTime.value = true
            result = false
        }
        if (description.isEmpty()) {
            _errorInputHelpType.value = true
            result = false
        }
        if (!AllUtils().isItPhone(phone)) {
            _errorInputInvalidPhone.value = true
            result = false
        }
        if (spinnerTypeSelected.value.isNullOrEmpty()) {
            _errorInputHelpType.value = true
            result = false
        }
        if (spinnerAgeSelected.value.isNullOrEmpty()) {
            _errorInputVolAge.value = true
            result = false
        }
        if (spinnerGenderSelected.value.isNullOrEmpty()) {
            _errorInputVolGender.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputHelpType() = _errorInputHelpType.postValue(false)
    fun resetErrorInputVolGender() = _errorInputVolGender.postValue(false)
    fun resetErrorInputVolAge() = _errorInputVolAge.postValue(false)
    fun resetErrorInputAddress() = _errorInputAddress.postValue(false)
    fun resetErrorInputHelpTime() = _errorInputHelpTime.postValue(false)
    fun resetErrorInputInvalidPhone() = _errorInputInvalidPhone.postValue(false)

    private fun parseStroke(input: String?) = input?.trim() ?: ""

    private fun stringToDateLong(date: String) =
        try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            sdf.parse(date)?.time ?: -1L
        } catch (e: Exception) {
            -1L
        }
}