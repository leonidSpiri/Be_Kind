package ru.spiridonov.be.kind.presentation.create_work

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.usecases.invalid_item.GetInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.volunteer_item.GetVolunteerItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.CreateWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.EditWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetWorkListUseCase
import ru.spiridonov.be.kind.utils.AllUtils
import ru.spiridonov.be.kind.utils.SharedPref
import java.util.*
import javax.inject.Inject

class HelpViewModel @Inject constructor(
    private val getInvalidItemUseCase: GetInvalidItemUseCase,
    private val getVolunteerItemUseCase: GetVolunteerItemUseCase,
    private val createWorkItemUseCase: CreateWorkItemUseCase,
    private val editWorkItemUseCase: EditWorkItemUseCase,
    private val getWorkListUseCase: GetWorkListUseCase,
    private val getWorkItemUseCase: GetWorkItemUseCase
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
    private val _errorInputInvalidPhone = MutableLiveData<Boolean>()
    val errorInputInvalidPhone: LiveData<Boolean>
        get() = _errorInputInvalidPhone

    private val _workList = MutableLiveData<List<WorkItem>>()
    val workList: LiveData<List<WorkItem>>
        get() = _workList

    val spinnerTypeSelected = MutableLiveData<String>()
    val spinnerGenderSelected = MutableLiveData<String>()
    val spinnerAgeSelected = MutableLiveData<String>()
    val selectedDate = MutableLiveData<Long>()

    private var uuid = ""

    fun hasUserActiveWork() = SharedPref().getSharedPref("activeWork") != ""

    fun approveWork(workItem: WorkItem) =
        getVolunteerUserInfo { volunteerItem ->
            if (volunteerItem != null) {
                viewModelScope.launch {
                    editWorkItemUseCase.invoke(
                        workItem.copy(
                            volunteerPhone = volunteerItem.personalPhone,
                            whoHelpId = volunteerItem.uuid,
                            status = "??????????????"
                        )
                    )
                    SharedPref().setSharedPref("activeWork", workItem.id)
                }
            }
        }

    fun getWorkList() =
        viewModelScope.launch {
            getWorkListUseCase.invoke {
                _workList.postValue(it)
            }
        }

    fun getWorkItem(id: String, callback: (WorkItem?) -> Unit) =
        viewModelScope.launch {
            getWorkItemUseCase.invoke(id) {
                callback(it)
            }
        }

    fun getInvalidUserInfo(callback: (InvalidItem?) -> Unit) =
        viewModelScope.launch {
            getInvalidItemUseCase.invoke { invalidItem ->
                if (invalidItem != null) {
                    uuid = invalidItem.uuid
                    callback(invalidItem)
                }
            }
        }

    private fun getVolunteerUserInfo(callback: (VolunteerItem?) -> Unit) =
        viewModelScope.launch {
            getVolunteerItemUseCase.invoke { volunteerItem ->
                if (volunteerItem != null) {
                    callback(volunteerItem)
                }
            }
        }

    fun createInvalidWork(address: String, description: String, phone: String) {
        val randomUUID = UUID.randomUUID().toString()
        val idStr = (uuid.dropLast(uuid.length / 2) + (randomUUID.dropLast(randomUUID.length / 2)))
        if (validateInput(idStr, address, description, phone)) {
            viewModelScope.launch {
                createWorkItemUseCase.invoke(
                    WorkItem(
                        id = idStr,
                        description = parseStroke(description),
                        whoNeedHelpId = uuid,
                        timestamp = System.currentTimeMillis(),
                        whenNeedHelp = selectedDate.value!!,
                        kindOfHelp = spinnerTypeSelected.value!!,
                        invalidPhone = phone,
                        address = parseStroke(address),
                        volunteerAge = spinnerAgeSelected.value!!,
                        volunteerGender = spinnerGenderSelected.value!!,
                        status = "??????????????",
                        doneCode = kotlin.random.Random.nextInt(12345, 98765).toString()
                    )
                )
                SharedPref().setSharedPref("activeWork", idStr)
            }
        }
    }

    private fun validateInput(
        id: String?,
        address: String,
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
        if (selectedDate.value == null) {
            result = false
        }
        return result
    }

    fun resetErrorInputHelpType() = _errorInputHelpType.postValue(false)
    fun resetErrorInputVolGender() = _errorInputVolGender.postValue(false)
    fun resetErrorInputVolAge() = _errorInputVolAge.postValue(false)
    fun resetErrorInputAddress() = _errorInputAddress.postValue(false)
    fun resetErrorInputInvalidPhone() = _errorInputInvalidPhone.postValue(false)

    private fun parseStroke(input: String?) = input?.trim() ?: ""
}