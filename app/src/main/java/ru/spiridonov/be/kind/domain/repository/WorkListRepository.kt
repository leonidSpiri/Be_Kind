package ru.spiridonov.be.kind.domain.repository

import androidx.lifecycle.LiveData
import ru.spiridonov.be.kind.domain.entity.WorkItem

interface WorkListRepository {
    suspend fun editWorkItem(workItem: WorkItem)

    suspend fun getGeneralWorkList(): Pair<String, String>

    fun getWorkList(): LiveData<List<WorkItem>>

    fun getWorkItem(workItemId: Int): LiveData<WorkItem>

    fun loadWorkData()
}