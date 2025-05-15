package data.datasource.local.csv

import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.DataSource
import org.example.domain.NoProjectsFoundException
import org.example.domain.ProjectNotFoundException
import org.example.domain.entity.Project
import java.util.*

class ProjectsCsvStorage(
    private val fileManager: CsvFileManager<Project>,
) : DataSource<Project> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoProjectsFoundException() }
    override fun addItem(newItem: Project) = fileManager.append(newItem)
    override fun deleteItem(item: Project) = fileManager.delete(item).let { if (!it) throw ProjectNotFoundException() }
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw ProjectNotFoundException()
    override fun updateItem(updatedItem: Project) =
        fileManager.update(updatedItem).let { if (!it) throw ProjectNotFoundException() }
}