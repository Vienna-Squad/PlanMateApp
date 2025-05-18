package org.example.data.datasource.remote.mongo

import org.example.data.datasource.UnEditableDataSource
import org.example.data.datasource.remote.mongo.manager.base.UnEditableMongoManager
import org.example.domain.entity.log.Log
import org.example.domain.exceptions.AdditionException
import org.example.domain.exceptions.NoLogsFoundException

class LogsMongoStorage(
    private val manager: UnEditableMongoManager<Log>
) : UnEditableDataSource<Log> {
    override fun getAllItems() = manager.readAll().ifEmpty { throw NoLogsFoundException() }
    override fun addItem(newItem: Log) = manager.append(newItem).let { if (!it) throw AdditionException() }
}