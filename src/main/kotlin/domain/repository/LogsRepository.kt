package org.example.domain.repository

import org.example.domain.entity.Log

interface LogsRepository {
    fun getAllLogs(): List<Log>
    fun addLog(log: Log)
}