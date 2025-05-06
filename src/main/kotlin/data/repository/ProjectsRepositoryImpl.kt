package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.data.utils.safeCall
import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsRemoteDataSource: RemoteDataSource<Project>,
    private val projectsLocalDataSource: LocalDataSource<Project>,
    private val preferences: Preference
) : ProjectsRepository {

    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getById(projectId).let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            project
        }
    }

    override fun getAllProjects() = safeCall {
        projectsRemoteDataSource.getAll().ifEmpty { throw NotFoundException("projects") }
    }

    override fun updateProject(updatedProject: Project) = authSafeCall { currentUser ->
        if (updatedProject.createdBy != currentUser.id) throw AccessDeniedException()
        projectsRemoteDataSource.update(updatedProject)
    }

    override fun addProject(name: String) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        projectsRemoteDataSource.add(
            Project(
                name = name,
                createdBy = currentUser.id,
            )
        )
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getById(projectId).let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsRemoteDataSource.delete(project)
        }
    }
}