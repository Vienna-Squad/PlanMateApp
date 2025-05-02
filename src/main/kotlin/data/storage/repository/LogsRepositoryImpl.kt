package org.example.data.storage.repository

import org.example.data.storage.LogsCsvStorage
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val storage: LogsCsvStorage
) : LogsRepository {

    override fun getAllLogs(): Result<List<Log>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun addLog(log: Log): Result<Unit> {
        return runCatching {
            storage.append(log)
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }
}