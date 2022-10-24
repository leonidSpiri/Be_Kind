package ru.spiridonov.be.kind.domain.usecases.work_list

import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.repository.WorkListRepository
import javax.inject.Inject

class GetLocalWorkItemUseCase @Inject constructor(
    private val repository: WorkListRepository
) {
    operator fun invoke(callback: (WorkItem) -> Unit) = repository.getLocalWorkItem(callback)
}