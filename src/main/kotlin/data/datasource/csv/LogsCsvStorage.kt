package data.datasource.csv

import org.example.common.bases.UnEditableCsvFileManager
import org.example.common.bases.UnEditableDataSource
import org.example.domain.NotFoundException
import org.example.domain.entity.log.Log

class LogsCsvStorage(
    private val fileManager: UnEditableCsvFileManager<Log>
) : UnEditableDataSource<Log> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NotFoundException("logs") }
    override fun addItem(newItem: Log) = fileManager.append(newItem)
}