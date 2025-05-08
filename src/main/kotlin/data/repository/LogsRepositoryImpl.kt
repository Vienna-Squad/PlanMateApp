package org.example.data.repository

import data.datasource.DataSource
import org.example.data.utils.SafeExecutor
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsDataSource: DataSource<Log>,
    private val safeExecutor: SafeExecutor
) : LogsRepository {

    override fun getAllLogs() = safeExecutor.call {
        logsDataSource.getAll()
    }

    override fun addLog(log: Log) = safeExecutor.call {
        logsDataSource.add(log)
    }
}

