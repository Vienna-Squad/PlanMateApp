package data.datasource.local.csv

import org.example.data.datasource.UnEditableDataSource
import org.example.data.datasource.local.csv.manager.base.UnEditableCsvFileManager
import org.example.domain.entity.log.Log
import org.example.domain.exceptions.NoLogsFoundException

class LogsCsvStorage(
    private val fileManager: UnEditableCsvFileManager<Log>
) : UnEditableDataSource<Log> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoLogsFoundException() }
    override fun addItem(newItem: Log) = fileManager.append(newItem)
}