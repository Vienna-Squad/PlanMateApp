package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import java.util.*

class LogsRepositoryImpl(
    //private val logsRemoteStorage: RemoteDataSource<Log>,
    private val logsLocalStorage: LocalDataSource<Log>,
    private val preferences: Preference
) : LogsRepository {
    override fun getAllLogs(id: UUID) =
        logsLocalStorage.getAll().filter { it.affectedId == id }.let { logs ->
            logsLocalStorage.getAll().filter { it.affectedId == id }.ifEmpty { throw NotFoundException("logs") }
        }.ifEmpty { throw NotFoundException("logs") }

    override fun addLog(log: Log) =
        logsLocalStorage.add(log)
}

