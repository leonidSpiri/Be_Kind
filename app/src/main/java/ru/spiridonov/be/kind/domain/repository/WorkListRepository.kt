package ru.spiridonov.be.kind.domain.repository

import ru.spiridonov.be.kind.domain.entity.WorkItem

interface WorkListRepository {
    suspend fun editWorkItem(workItem: WorkItem)

    suspend fun getFilterWorkList(query: String, callback: (List<WorkItem>) -> Unit)

    suspend fun getWorkList(callback: (List<WorkItem>) -> Unit)

    suspend fun getWorkItem(workItemId: String, callback: (WorkItem) -> Unit)

    fun getLocalWorkItem(callback: (WorkItem) -> Unit)
}