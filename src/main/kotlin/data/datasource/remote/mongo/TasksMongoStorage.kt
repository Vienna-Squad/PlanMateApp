package data.datasource.remote.mongo

import org.example.data.datasource.DataSource
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.domain.entity.Task
import org.example.domain.exceptions.AdditionException
import org.example.domain.exceptions.DeletionException
import org.example.domain.exceptions.ModificationException
import org.example.domain.exceptions.NoTasksFoundException
import org.example.domain.exceptions.TaskNotFoundException
import java.util.*

class TasksMongoStorage(
    private val manager: MongoManager<Task>
) : DataSource<Task> {
    override fun getItemById(id: UUID) = manager.getById(id) ?: throw TaskNotFoundException()

    override fun deleteItem(item: Task) = manager.delete(item).let { if (!it) throw DeletionException() }

    override fun updateItem(updatedItem: Task) =
        manager.update(updatedItem).let { if (!it) throw ModificationException() }

    override fun getAllItems() = manager.readAll().ifEmpty { throw NoTasksFoundException() }

    override fun addItem(newItem: Task) = manager.append(newItem).let { if (!it) throw AdditionException() }
}