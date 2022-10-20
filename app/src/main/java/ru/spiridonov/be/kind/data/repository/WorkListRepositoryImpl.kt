package ru.spiridonov.be.kind.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import ru.spiridonov.be.kind.data.mapper.WorkListMapper
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.repository.WorkListRepository
import javax.inject.Inject

class WorkListRepositoryImpl @Inject constructor(
    private val mapper: WorkListMapper
) : WorkListRepository {
    override suspend fun editWorkItem(workItem: WorkItem) {
        Log.d("TAG", "editWorkItem: $workItem")
    }

    override suspend fun getGeneralWorkList(): Pair<String, String> {
        TODO("Not yet implemented")
    }

    override fun getWorkList(): LiveData<List<WorkItem>> {
        TODO("Not yet implemented")
    }

    override fun getWorkItem(workItemId: Int): LiveData<WorkItem> {
        TODO("Not yet implemented")
    }

    override fun loadWorkData() {
        TODO("Not yet implemented")
    }
}