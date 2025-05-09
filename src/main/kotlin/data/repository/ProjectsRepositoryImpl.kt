package org.example.data.repository

import data.datasource.DataSource
import org.example.data.utils.safeCall
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: DataSource<Project>,
) : ProjectsRepository {
    override fun getProjectById(projectId: UUID) = safeCall { projectsDataSource.getById(projectId) }
    override fun getAllProjects() = safeCall { projectsDataSource.getAll() }
    override fun addProject(project: Project) = safeCall { projectsDataSource.add(project) }
    override fun updateProject(updatedProject: Project) =
        safeCall { projectsDataSource.update(updatedProject) }

    override fun deleteProjectById(projectId: UUID) =
        safeCall { projectsDataSource.delete(getProjectById(projectId)) }
}