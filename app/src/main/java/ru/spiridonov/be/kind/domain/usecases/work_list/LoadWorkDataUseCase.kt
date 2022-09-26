package ru.spiridonov.be.kind.domain.usecases.work_list

import ru.spiridonov.be.kind.domain.repository.WorkListRepository
import javax.inject.Inject

class LoadWorkDataUseCase @Inject constructor(
    private val repository: WorkListRepository
) {
    operator fun invoke() = repository.loadWorkData()
}