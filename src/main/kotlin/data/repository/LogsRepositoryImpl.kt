package org.example.data.repository

import data.storage.Storage
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logStorage: Storage<Log>
) : LogsRepository {
    override fun getAll(): Result<List<Log>> {
        TODO("Not yet implemented")
    }

    override fun add(log: Log): Result<Unit> {
        TODO("Not yet implemented")
    }
}