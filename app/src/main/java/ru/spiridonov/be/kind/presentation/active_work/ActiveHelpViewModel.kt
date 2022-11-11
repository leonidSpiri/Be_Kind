package ru.spiridonov.be.kind.presentation.active_work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.domain.entity.InvalidItem
import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.usecases.invalid_item.GetInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.volunteer_item.GetVolunteerItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.EditWorkItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetWorkItemUseCase
import javax.inject.Inject

class ActiveHelpViewModel @Inject constructor(
    private val getInvalidItemUseCase: GetInvalidItemUseCase,
    private val getVolunteerItemUseCase: GetVolunteerItemUseCase,
    private val editWorkItemUseCase: EditWorkItemUseCase,
    private val getWorkItemUseCase: GetWorkItemUseCase
) : ViewModel() {
    fun getWorkItem(id: String, callback: (WorkItem?) -> Unit) =
        viewModelScope.launch {
            getWorkItemUseCase.invoke(id) {
                callback(it)
            }
        }
}