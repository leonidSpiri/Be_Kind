package ru.spiridonov.be.kind.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spiridonov.be.kind.data.mapper.AccountItemMapper
import ru.spiridonov.be.kind.domain.entity.AccountItem
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.usecases.account_item.IsUserLoggedInUseCase
import ru.spiridonov.be.kind.domain.usecases.invalid_item.GetInvalidItemUseCase
import ru.spiridonov.be.kind.domain.usecases.volunteer_item.GetVolunteerItemUseCase
import ru.spiridonov.be.kind.domain.usecases.work_list.GetLocalWorkItemUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getVolunteerItemUseCase: GetVolunteerItemUseCase,
    private val getInvalidItemUseCase: GetInvalidItemUseCase,
    private val getLocalWorkItemUseCase: GetLocalWorkItemUseCase,
    private val accountItemMapper: AccountItemMapper
) : ViewModel() {
    private val _workItem = MutableLiveData<WorkItem?>()
    val workItem: LiveData<WorkItem?>
        get() = _workItem

    fun getWork() = getLocalWorkItemUseCase.invoke {
        _workItem.postValue(it)
    }

    fun isUserLoggedIn() = isUserLoggedInUseCase()

    fun getUserInfo(callback: (AccountItem?) -> Unit) {
        getVolunteerItemUseCase.invoke { volunteerItem ->
            if (volunteerItem != null) {
                var accountItem = accountItemMapper.mapVolunteerItemToAccountItem(volunteerItem)
                accountItem =
                    accountItem.copy(surName = "${accountItem.surName} ${accountItem.name} ${accountItem.lastname}")
                callback(accountItem)
            }
        }
        getInvalidItemUseCase.invoke { invalidItem ->
            if (invalidItem != null) {
                var accountItem = accountItemMapper.mapInvalidItemToAccountItem(invalidItem)
                accountItem =
                    accountItem.copy(surName = "${accountItem.surName} ${accountItem.name} ${accountItem.lastname}")
                callback(accountItem)
            }
            callback(null)
        }
    }
}