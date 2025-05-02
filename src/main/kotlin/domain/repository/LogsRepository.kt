package org.example.domain.repository

import org.example.domain.entity.Log

interface LogsRepository {
    fun getAll(): Result<List<Log>>
    fun add(log: Log): Result<Unit>
}