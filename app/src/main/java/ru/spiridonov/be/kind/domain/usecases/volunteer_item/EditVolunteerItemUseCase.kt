package ru.spiridonov.be.kind.domain.usecases.volunteer_item

import ru.spiridonov.be.kind.domain.entity.VolunteerItem
import ru.spiridonov.be.kind.domain.repository.VolunteerItemRepository
import javax.inject.Inject

class EditVolunteerItemUseCase @Inject constructor(
    private val repository: VolunteerItemRepository
) {
    suspend operator fun invoke(volunteerItem: VolunteerItem) =
        repository.editVolunteerItem(volunteerItem)
}