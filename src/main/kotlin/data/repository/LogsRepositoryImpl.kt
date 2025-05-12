package org.example.data.repository

import org.example.common.bases.UnEditableDataSource
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsDataSource: UnEditableDataSource<Log>,
) : LogsRepository {
    override fun getAllLogs() = safeCall { logsDataSource.getAllItems() }
    override fun addLog(log: Log) = safeCall { logsDataSource.addItem(log) }
}

