package ru.spiridonov.be.kind.domain.usecases.volunteer_item

import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.VolunteerItemRepository
import javax.inject.Inject

class GetVolunteerItemUseCase @Inject constructor(
    private val repository: VolunteerItemRepository
) {
    operator fun invoke(callback: (VolunteerItem?) -> Unit) = repository.getVolunteerItem(callback)
}