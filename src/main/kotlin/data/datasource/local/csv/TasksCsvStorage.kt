package data.datasource.local.csv

import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.DataSource
import org.example.domain.NoTasksFoundException
import org.example.domain.TaskNotFoundException
import org.example.domain.entity.Task
import java.util.*

class TasksCsvStorage(
    private val fileManager: CsvFileManager<Task>,
) : DataSource<Task> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoTasksFoundException() }
    override fun addItem(newItem: Task) = fileManager.append(newItem)
    override fun deleteItem(item: Task) = fileManager.delete(item).let { if (!it) throw TaskNotFoundException() }
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw TaskNotFoundException()
    override fun updateItem(updatedItem: Task) =
        fileManager.update(updatedItem).let { if (!it) throw TaskNotFoundException() }
}