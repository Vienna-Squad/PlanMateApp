package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.CsvPreferences
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsRemoteStorage: RemoteDataSource<Project>,
    private val projectsLocalStorage: LocalDataSource<Project>,
    private val preferences: CsvPreferences
) : ProjectsRepository {
    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            project
        } ?: throw NotFoundException("project")
    }

    override fun getAllProjects() = projectsLocalStorage.getAll().ifEmpty { throw NotFoundException("projects") }

    override fun addMateToProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in project.matesIds) throw AlreadyExistException()
            projectsLocalStorage.update(project.copy(matesIds = project.matesIds + mateId))
        } ?: throw NotFoundException("project")
    }

    override fun updateProject(updatedProject: Project) = authSafeCall { currentUser ->
        if (updatedProject.createdBy == currentUser.id) throw AccessDeniedException()
        projectsLocalStorage.update(updatedProject)
    }

    override fun addStateToProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (state in project.states) throw AlreadyExistException()
            projectsLocalStorage.update(project.copy(states = project.states + state))
        } ?: throw NotFoundException("project")
    }

    override fun addProject(name: String) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        projectsLocalStorage.add(
            Project(
                name = name,
                createdBy = currentUser.id,
            )
        )
    }


    override fun editProjectName(projectId: UUID, name: String) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsLocalStorage.update(project.copy(name = name))
        } ?: throw NotFoundException("project")
    }

    override fun deleteMateFromProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val mates = project.matesIds.toMutableList()
            mates.removeIf { it == mateId }
            projectsLocalStorage.update(project.copy(matesIds = mates))
        } ?: throw NotFoundException("project")
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsLocalStorage.delete(project)
        } ?: throw NotFoundException("project")
    }

    override fun deleteStateFromProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsLocalStorage.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val states = project.states.toMutableList()
            states.removeIf { it == state }
            projectsLocalStorage.update(project.copy(states = states))
        } ?: throw NotFoundException("project")
    }
}
