package org.example.data.repository

import data.datasource.DataSource
import org.example.data.utils.authSafeCall
import org.example.data.utils.safeCall
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsDataSource: DataSource<Log>,
) : LogsRepository {

    override fun getAllLogs() = safeCall {
        logsDataSource.getAll().also { logs ->
            if (logs.isEmpty()) {
                throw NotFoundException("logs")
            }
        }
    }

    override fun addLog(log: Log) = authSafeCall { currentUser ->
        logsDataSource.add(log.apply {
            username = currentUser.username
        })
    }
}

