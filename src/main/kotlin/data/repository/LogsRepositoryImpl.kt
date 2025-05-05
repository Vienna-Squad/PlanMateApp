package org.example.data.repository

import org.example.data.datasource.local.csv.LogsCsvStorage
import org.example.data.datasource.remote.RemoteDataSource
import org.example.domain.NotFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import java.util.*

class LogsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource<Log>,
    //private val localDataSource: LocalDataSource<Log>,
    private val logsStorage: LogsCsvStorage,
) : LogsRepository {
    override fun getAllLogs(id: UUID) =
        logsStorage.read().filter { it.affectedId == id }.let { logs ->
            logsStorage.read().filter { it.affectedId == id }.ifEmpty { throw NotFoundException("logs") }
        }.ifEmpty { throw NotFoundException("logs") }


    override fun addLog(log: Log) =
        logsStorage.append(log)

}

