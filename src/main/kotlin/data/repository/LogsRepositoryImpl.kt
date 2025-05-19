package org.example.data.repository

import org.example.data.datasource.UnEditableDataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository

class LogsRepositoryImpl(
    private val localDataSource: UnEditableDataSource<Log>,
    private val remoteDataSource: UnEditableDataSource<Log>,
) : LogsRepository {
    override fun getAllLogs() = safeCall {
        if (isRemote()) {
            remoteDataSource.getAllItems()
        } else {
            localDataSource.getAllItems()
        }
    }

    override fun addLog(log: Log) = safeCall {
        if (isRemote()) {
            remoteDataSource.addItem(log)
        } else {
            localDataSource.addItem(log)
        }
    }
}

