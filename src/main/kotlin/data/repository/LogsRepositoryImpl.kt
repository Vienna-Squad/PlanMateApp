package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsRemoteDataSource: RemoteDataSource<Log>,
    private val logsLocalDataSource: LocalDataSource<Log>,
    private val preferences: Preference
) : LogsRepository {

    override fun getAllLogs(): List<Log> {
        val logs = logsRemoteDataSource.getAll()
        if (logs.isEmpty()) {
            throw NotFoundException("logs")
        }
        return logs
    }

    override fun addLog(log: Log) = logsRemoteDataSource.add(log)
}

