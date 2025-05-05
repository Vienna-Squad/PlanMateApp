package org.example.domain.repository

import org.example.domain.entity.Log
import java.util.UUID

interface LogsRepository {
    fun getAllLogs(id: UUID): List<Log>
    fun addLog(log: Log)
}