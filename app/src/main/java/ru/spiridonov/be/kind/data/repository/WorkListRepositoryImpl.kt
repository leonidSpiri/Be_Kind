package ru.spiridonov.be.kind.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.spiridonov.be.kind.domain.entity.WorkItem
import ru.spiridonov.be.kind.domain.repository.WorkListRepository
import java.sql.DriverManager
import java.sql.Statement
import javax.inject.Inject

class WorkListRepositoryImpl @Inject constructor(
) : WorkListRepository {

    override suspend fun editWorkItem(workItem: WorkItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Class.forName("org.postgresql.Driver")
                url = String.format(url, host, port, database)
                val connection = DriverManager.getConnection(url, user, pass)
                val st: Statement = connection.createStatement()
                st.execute(
                    String.format(
                        insert, workItem.id,
                        workItem.description,
                        workItem.whoNeedHelpId,
                        workItem.timestamp.toString(),
                        workItem.whenNeedHelp.toString(),
                        workItem.kindOfHelp,
                        workItem.invalidPhone,
                        workItem.address,
                        workItem.volunteerAge,
                        workItem.volunteerGender
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

    companion object {
        private const val host = "ec2-63-32-248-14.eu-west-1.compute.amazonaws.com"
        private const val database = "dbbl30gd4a1qpu"
        private const val port = 5432
        private const val user = "ygaxqwmkigdfoe"
        private const val pass = "72bc02774a938cdd81c33e5e3d56bab6ad52e79f64b878d9284387c4e04ce930"
        private var url = "jdbc:postgresql://%s:%d/%s"
        private var insert =
            "insert into work_items (id, description, isDone, whoNeedHelpId, timeStampNow, whenNeedHelp, kindOfHelp, invalidPhone, address, volunteerAge, volunteerGender)\nVALUES ('%s', '%s', false, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');"

    }
}