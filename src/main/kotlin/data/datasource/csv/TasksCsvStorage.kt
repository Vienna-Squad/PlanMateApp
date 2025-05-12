package data.datasource.csv

import org.example.common.bases.CsvFileManager
import org.example.common.bases.DataSource
import org.example.data.datasource.csv.manager.TasksCsvFileManager
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import java.util.*

class TasksCsvStorage(
    private val fileManager: CsvFileManager<Task> = TasksCsvFileManager(),
) : DataSource<Task> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NotFoundException() }
    override fun addItem(newItem: Task) = fileManager.append(newItem)
    override fun deleteItem(item: Task) = fileManager.delete(item)
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw NotFoundException()
    override fun updateItem(updatedItem: Task) =
        fileManager.update(updatedItem).let { if (!it) throw NotFoundException() }
}