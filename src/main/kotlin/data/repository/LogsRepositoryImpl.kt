package org.example.data.repository

import org.example.data.datasource.mongo.LogsMongoStorage
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import java.util.*

class LogsRepositoryImpl(
    private val logsStorage: LogsMongoStorage
) : LogsRepository {

    override fun getAllLogs(id: UUID): List<Log> {
        val logs = logsStorage.getLogsByAffectedId(id)
        if (logs.isEmpty()) {
            throw NotFoundException("logs")
        }
        return logs
    }

    override fun addLog(log: Log) {
        logsStorage.add(log)
    }
}

