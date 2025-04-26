package org.example.domain.repository

import org.example.domain.entity.Log

interface LogsRepository {
    fun getLogs(): List<Log>
    fun addLog(log: Log)
}