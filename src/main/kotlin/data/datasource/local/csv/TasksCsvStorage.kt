package data.datasource.local.csv

import org.example.data.datasource.DataSource
import org.example.data.datasource.local.csv.manager.base.CsvFileManager
import org.example.domain.entity.Task
import org.example.domain.exceptions.DeletionException
import org.example.domain.exceptions.ModificationException
import org.example.domain.exceptions.NoTasksFoundException
import org.example.domain.exceptions.TaskNotFoundException
import java.util.*

class TasksCsvStorage(
    private val fileManager: CsvFileManager<Task>,
) : DataSource<Task> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoTasksFoundException() }
    override fun addItem(newItem: Task) = fileManager.append(newItem)
    override fun deleteItem(item: Task) = fileManager.delete(item).let { if (!it) throw DeletionException() }
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw TaskNotFoundException()
    override fun updateItem(updatedItem: Task) =
        fileManager.update(updatedItem).let { if (!it) throw ModificationException() }
}