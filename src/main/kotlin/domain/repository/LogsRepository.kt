package org.example.domain.repository

import org.example.domain.entity.log.Log

interface LogsRepository : Repository {
    fun getAllLogs(): List<Log>
    fun addLog(log: Log)
}