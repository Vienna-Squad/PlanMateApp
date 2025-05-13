package org.example.data.repository

import org.example.common.bases.DataSource
import org.example.common.bases.UnEditableDataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsLocalDataSource: UnEditableDataSource<Log>,
    private val logsRemoteDataSource: DataSource<Log>,
) : LogsRepository {
    override fun getAllLogs() = safeCall {
        if (isRemote()) {
            logsRemoteDataSource.getAllItems()
        } else {
            logsLocalDataSource.getAllItems()
        }
    }

    override fun addLog(log: Log) = safeCall {
        if (isRemote()) {
            logsRemoteDataSource.addItem(log)
        } else {
            logsLocalDataSource.addItem(log)
        }
    }
}

