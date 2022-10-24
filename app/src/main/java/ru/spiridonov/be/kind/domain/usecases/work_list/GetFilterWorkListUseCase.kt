package ru.spiridonov.be.kind.domain.usecases.work_list

import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.repository.WorkListRepository
import javax.inject.Inject

class GetFilterWorkListUseCase @Inject constructor(
    private val repository: WorkListRepository
) {
    suspend operator fun invoke(query: String, callback: (List<WorkItem>) -> Unit) =
        repository.getFilterWorkList(query, callback)
}