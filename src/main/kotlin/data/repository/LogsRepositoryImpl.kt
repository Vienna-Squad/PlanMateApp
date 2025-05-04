package org.example.data.repository

import org.example.data.datasource.csv.LogsCsvStorage
import org.example.data.datasource.csv.ProjectsCsvStorage
import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.data.repository.Repository
import java.util.*

class LogsRepositoryImpl(
    private val logsStorage: LogsCsvStorage,
) : Repository(), LogsRepository {
    override fun getAllLogs(id: UUID) = safeCall {
        logsStorage.read().filter { it.affectedId == id }.let { logs ->
            logsStorage.read().filter { it.affectedId == id }.ifEmpty { throw NotFoundException("logs") }
        }.ifEmpty { throw NotFoundException("logs") }
    }

    override fun addLog(log: Log) = safeCall {
        logsStorage.append(log)
    }
}

