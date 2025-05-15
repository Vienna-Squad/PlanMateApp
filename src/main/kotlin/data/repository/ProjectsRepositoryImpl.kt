package org.example.data.repository

import org.example.data.datasource.DataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val localDataSource: DataSource<Project>,
    private val remoteDataSource: DataSource<Project>,
) : ProjectsRepository {
    override fun getProjectById(projectId: UUID) = safeCall {
        if (isRemote()) {
            remoteDataSource.getItemById(projectId)
        } else {
            localDataSource.getItemById(projectId)
        }
    }

    override fun getAllProjects() = safeCall {
        if (isRemote()) {
            remoteDataSource.getAllItems()
        } else {
            localDataSource.getAllItems()
        }
    }

    override fun addProject(project: Project) = safeCall {
        if (isRemote()) {
            remoteDataSource.addItem(project)
        } else {
            localDataSource.addItem(project)
        }
    }

    override fun updateProject(updatedProject: Project) =
        safeCall {
            if (isRemote()) {
                remoteDataSource.updateItem(updatedProject)
            } else {
                localDataSource.updateItem(updatedProject)
            }
        }

    override fun deleteProjectById(projectId: UUID) =
        safeCall {
            if (isRemote()) {
                remoteDataSource.deleteItem(getProjectById(projectId))
            } else {
                localDataSource.deleteItem(getProjectById(projectId))
            }
        }
}