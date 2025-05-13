package org.example.data.repository

import org.example.common.bases.DataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsLocalDataSource: DataSource<Project>,
    private val projectsRemoteDataSource: DataSource<Project>,
) : ProjectsRepository {
    override fun getProjectById(projectId: UUID) = safeCall {
        if (isRemote()) {
            projectsRemoteDataSource.getItemById(projectId)
        } else {
            projectsLocalDataSource.getItemById(projectId)
        }
    }

    override fun getAllProjects() = safeCall {
        if (isRemote()) {
            projectsRemoteDataSource.getAllItems()
        } else {
            projectsLocalDataSource.getAllItems()
        }
    }

    override fun addProject(project: Project) = safeCall {
        if (isRemote()) {
            projectsRemoteDataSource.addItem(project)
        } else {
            projectsLocalDataSource.addItem(project)
        }
    }

    override fun updateProject(updatedProject: Project) =
        safeCall {
            if (isRemote()) {
                projectsRemoteDataSource.updateItem(updatedProject)
            } else {
                projectsLocalDataSource.updateItem(updatedProject)
            }
        }

    override fun deleteProjectById(projectId: UUID) =
        safeCall {
            if (isRemote()) {
                projectsRemoteDataSource.deleteItem(getProjectById(projectId))
            } else {
                projectsLocalDataSource.deleteItem(getProjectById(projectId))
            }
        }
}