package ru.spiridonov.be.kind.presentation.active_work

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.usecases.work_list.EditWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetLocalWorkItemUseCase
import javax.inject.Inject

class ActiveHelpViewModel @Inject constructor(
    private val editWorkItemUseCase: EditWorkItemUseCase,
    private val getLocalWorkItemUseCase: GetLocalWorkItemUseCase
) : ViewModel() {

    private val _workItem = MutableLiveData<WorkItem?>()
    val workItem: LiveData<WorkItem?>
        get() = _workItem
    private val _errorInputFinishCode = MutableLiveData<Boolean>()
    val errorInputFinishCode: LiveData<Boolean>
        get() = _errorInputFinishCode

    private fun getWorkItem() = getLocalWorkItemUseCase.invoke {
        _workItem.postValue(it)
    }

    fun setFinishStatus(workItem: WorkItem) =
        viewModelScope.launch {
            editWorkItemUseCase.invoke(
                workItem.copy(status = "Завершено")
            )
        }

    fun endVolunteerWork(code: String, workItem: WorkItem): Boolean {
        if (validateFinishCode(code)) {
            viewModelScope.launch {
                editWorkItemUseCase.invoke(
                    workItem.copy(isDone = true)
                )
            }
            return true
        }
        return false
    }

    fun resetErrorInputFinishCode() {
        _errorInputFinishCode.value = false
    }

    private fun validateFinishCode(code: String): Boolean {
        return if (code == workItem.value?.doneCode)
            true
        else {
            _errorInputFinishCode.value = true
            false
        }
    }

    init {
        getWorkItem()
    }
}