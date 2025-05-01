package org.example.data.storage.repository

import org.example.data.storage.LogsCsvStorage
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsCsvRepository (
    private val storage: LogsCsvStorage
) : LogsRepository {
    override fun getAll(): Result<List<Log>> {
        TODO("Not yet implemented")
    }

    override fun add(log: Log): Result<Unit> {
        TODO("Not yet implemented")
    }
}