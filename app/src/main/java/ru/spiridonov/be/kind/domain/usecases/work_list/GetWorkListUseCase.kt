package ru.spiridonov.be.kind.domain.usecases.work_list

import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.repository.WorkListRepository
import javax.inject.Inject

class GetWorkListUseCase @Inject constructor(
    private val repository: WorkListRepository
) {
    suspend operator fun invoke(callback: (List<WorkItem>) -> Unit) =
        repository.getWorkList(callback)
}