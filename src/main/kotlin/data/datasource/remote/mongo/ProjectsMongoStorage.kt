package data.datasource.remote.mongo

import org.example.data.datasource.DataSource
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.domain.entity.Project
import org.example.domain.exceptions.AdditionException
import org.example.domain.exceptions.DeletionException
import org.example.domain.exceptions.ModificationException
import org.example.domain.exceptions.NoProjectsFoundException
import org.example.domain.exceptions.ProjectNotFoundException
import java.util.*

class ProjectsMongoStorage(
    private val manager: MongoManager<Project>
) : DataSource<Project> {
    override fun getItemById(id: UUID) = manager.getById(id) ?: throw ProjectNotFoundException()

    override fun deleteItem(item: Project) = manager.delete(item).let { if (!it) throw DeletionException() }

    override fun updateItem(updatedItem: Project) =
        manager.update(updatedItem).let { if (!it) throw ModificationException() }

    override fun getAllItems() = manager.readAll().ifEmpty { throw NoProjectsFoundException() }

    override fun addItem(newItem: Project) = manager.append(newItem).let { if (!it) throw AdditionException() }
}