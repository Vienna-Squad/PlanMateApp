package data.datasource.local.csv

import org.example.data.datasource.DataSource
import org.example.data.datasource.local.csv.manager.base.CsvFileManager
import org.example.domain.entity.Project
import org.example.domain.exceptions.DeletionException
import org.example.domain.exceptions.ModificationException
import org.example.domain.exceptions.NoProjectsFoundException
import org.example.domain.exceptions.ProjectNotFoundException
import java.util.*

class ProjectsCsvStorage(
    private val fileManager: CsvFileManager<Project>,
) : DataSource<Project> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoProjectsFoundException() }
    override fun addItem(newItem: Project) = fileManager.append(newItem)
    override fun deleteItem(item: Project) = fileManager.delete(item).let { if (!it) throw DeletionException() }
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw ProjectNotFoundException()
    override fun updateItem(updatedItem: Project) = fileManager.update(updatedItem).let { if (!it) throw ModificationException() }
}