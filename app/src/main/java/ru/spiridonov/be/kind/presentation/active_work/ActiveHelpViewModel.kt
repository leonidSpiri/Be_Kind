package ru.spiridonov.be.kind.presentation.active_work

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.usecases.invalid_item.GetInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.volunteer_item.GetVolunteerItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.EditWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetLocalWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetWorkItemUseCase
import javax.inject.Inject

class ActiveHelpViewModel @Inject constructor(
    private val getInvalidItemUseCase: GetInvalidItemUseCase,
    private val getVolunteerItemUseCase: GetVolunteerItemUseCase,
    private val editWorkItemUseCase: EditWorkItemUseCase,
    private val getLocalWorkItemUseCase: GetLocalWorkItemUseCase,
    private val getWorkItemUseCase: GetWorkItemUseCase
) : ViewModel() {

    private val _workItem = MutableLiveData<WorkItem?>()
    val workItem: LiveData<WorkItem?>
        get() = _workItem

    private fun getWorkItem() = getLocalWorkItemUseCase.invoke {
        _workItem.postValue(it)
    }


    init {
        getWorkItem()
    }
}