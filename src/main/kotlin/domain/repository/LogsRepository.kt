package org.example.domain.repository

import org.example.domain.entity.Log

interface LogsRepository {
    fun getAllLogs(): Result<List<Log>>
    fun addLog(log: Log): Result<Unit>
}