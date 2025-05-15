package data.datasource.local.csv

import org.example.data.datasource.local.bases.UnEditableCsvFileManager
import org.example.data.datasource.UnEditableDataSource
import org.example.domain.LogsNotFoundException
import org.example.domain.entity.log.Log

class LogsCsvStorage(
    private val fileManager: UnEditableCsvFileManager<Log>
) : UnEditableDataSource<Log> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw LogsNotFoundException() }
    override fun addItem(newItem: Log) = fileManager.append(newItem)
}