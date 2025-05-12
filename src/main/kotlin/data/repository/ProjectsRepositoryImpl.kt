package org.example.data.repository

import org.example.common.bases.DataSource
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: DataSource<Project>,
) : ProjectsRepository {
    override fun getProjectById(projectId: UUID) = safeCall { projectsDataSource.getItemById(projectId) }
    override fun getAllProjects() = safeCall { projectsDataSource.getAllItems() }
    override fun addProject(project: Project) = safeCall { projectsDataSource.addItem(project) }
    override fun updateProject(updatedProject: Project) =
        safeCall { projectsDataSource.updateItem(updatedProject) }

    override fun deleteProjectById(projectId: UUID) =
        safeCall { projectsDataSource.deleteItem(getProjectById(projectId)) }
}