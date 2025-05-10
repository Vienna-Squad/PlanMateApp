package org.example.data.repository

import data.datasource.DataSource
import org.example.data.utils.safeCall
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsDataSource: DataSource<Log>,
) : LogsRepository {
    override fun getAllLogs() = safeCall { logsDataSource.getAll() }
    override fun addLog(log: Log) = safeCall { logsDataSource.add(log) }
}

