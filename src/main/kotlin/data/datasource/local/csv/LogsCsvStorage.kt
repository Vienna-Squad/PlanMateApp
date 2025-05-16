package data.datasource.csv

import org.example.common.bases.UnEditableCsvFileManager
import org.example.common.bases.UnEditableDataSource
import org.example.domain.entity.log.Log
import org.example.domain.exceptions.LogsNotFoundException

class LogsCsvStorage(
    private val fileManager: UnEditableCsvFileManager<Log>
) : UnEditableDataSource<Log> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw LogsNotFoundException() }
    override fun addItem(newItem: Log) = fileManager.append(newItem)
}