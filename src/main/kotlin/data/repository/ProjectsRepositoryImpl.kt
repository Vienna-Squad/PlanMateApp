package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsRemoteDataSource: RemoteDataSource<Project>,
    private val projectsLocalDataSource: LocalDataSource<Project>,
    private val preferences: Preference
) : ProjectsRepository {

    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            project
        } ?: throw NotFoundException("project")
    }

    override fun getAllProjects() =
        projectsRemoteDataSource.getAll().ifEmpty { throw NotFoundException("projects") }

    override fun addMateToProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in project.matesIds) throw AlreadyExistException()
            projectsRemoteDataSource.update(project.copy(matesIds = project.matesIds + mateId))
        } ?: throw NotFoundException("project")
    }

    override fun updateProject(updatedProject: Project) = authSafeCall { currentUser ->
        if (updatedProject.createdBy == currentUser.id) throw AccessDeniedException()
        projectsRemoteDataSource.update(updatedProject)
    }

    override fun addStateToProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (state in project.states) throw AlreadyExistException()
            projectsRemoteDataSource.update(project.copy(states = project.states + state))
        } ?: throw NotFoundException("project")
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

    override fun editProjectName(projectId: UUID, name: String) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsRemoteDataSource.update(project.copy(name = name))
        } ?: throw NotFoundException("project")
    }

    override fun deleteMateFromProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val mates = project.matesIds.toMutableList()
            mates.removeIf { it == mateId }
            projectsRemoteDataSource.update(project.copy(matesIds = mates))
        } ?: throw NotFoundException("project")
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsRemoteDataSource.delete(project)
        } ?: throw NotFoundException("project")
    }

    override fun deleteStateFromProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsRemoteDataSource.getAll().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val states = project.states.toMutableList()
            states.removeIf { it == state }
            projectsRemoteDataSource.update(project.copy(states = states))
        } ?: throw NotFoundException("project")
    }
}