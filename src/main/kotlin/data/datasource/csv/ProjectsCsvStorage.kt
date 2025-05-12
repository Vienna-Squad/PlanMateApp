package data.datasource.csv

import org.example.common.bases.CsvFileManager
import org.example.common.bases.DataSource
import org.example.data.datasource.csv.manager.ProjectsCsvFileManager
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import java.util.*

class ProjectsCsvStorage(
    private val fileManager: CsvFileManager<Project> = ProjectsCsvFileManager(),
) : DataSource<Project> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NotFoundException("projects") }
    override fun addItem(newItem: Project) = fileManager.append(newItem)
    override fun deleteItem(item: Project) = fileManager.delete(item)
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw NotFoundException("project")
    override fun updateItem(updatedItem: Project) =
        fileManager.update(updatedItem).let { if (!it) throw NotFoundException("project") }
}