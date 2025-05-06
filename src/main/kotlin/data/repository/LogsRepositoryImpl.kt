package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.data.utils.safeCall
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val logsRemoteDataSource: RemoteDataSource<Log>,
    private val logsLocalDataSource: LocalDataSource<Log>,
    private val preferences: Preference
) : LogsRepository {

    override fun getAllLogs() = safeCall {
        logsRemoteDataSource.getAll().also { logs ->
            if (logs.isEmpty()) {
                throw NotFoundException("logs")
            }
        }
    }

    override fun addLog(log: Log) = authSafeCall { currentUser ->
        logsRemoteDataSource.add(log.apply {
            username = currentUser.username
        })
    }
}

