package org.example.data.repository

import data.datasource.DataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: DataSource<Project>,
) : ProjectsRepository {

    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsDataSource.getById(projectId).let { project ->
            if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw AccessDeniedException()
            project
        }
    }

    override fun getAllProjects() = authSafeCall { projectsDataSource.getAll() }

    override fun addProject(project: Project) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        projectsDataSource.add(project)
    }

    override fun updateProject(updatedProject: Project) = authSafeCall { currentUser ->
        if (updatedProject.createdBy != currentUser.id) throw AccessDeniedException()
        projectsDataSource.update(updatedProject)
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsDataSource.getById(projectId).let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsDataSource.delete(project)
        }
    }
}