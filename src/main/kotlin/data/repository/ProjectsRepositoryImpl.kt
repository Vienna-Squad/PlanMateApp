package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsRemoteDataSource: RemoteDataSource<Project>,
    private val projectsLocalDataSource: LocalDataSource<Project>,
) : ProjectsRepository {

    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getById(projectId).let { project ->
            if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw AccessDeniedException()
            project
        }
    }

    override fun getAllProjects() = authSafeCall { projectsRemoteDataSource.getAll() }

    override fun addProject(project: Project) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        projectsRemoteDataSource.add(project)
    }

    override fun updateProject(updatedProject: Project) = authSafeCall { currentUser ->
        if (updatedProject.createdBy != currentUser.id) throw AccessDeniedException()
        projectsRemoteDataSource.update(updatedProject)
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getById(projectId).let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsRemoteDataSource.delete(project)
        }
    }
}