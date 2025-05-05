package org.example.data.repository

import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import java.util.*

import org.example.data.datasource.mongo.MongoPreferences
import org.example.data.datasource.mongo.ProjectsMongoStorage


class ProjectsRepositoryImpl(
    private val projectsStorage: ProjectsMongoStorage,
    private val preferences: MongoPreferences
) : ProjectsRepository {

    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            project
        } ?: throw NotFoundException("project")
    }

    override fun getAllProjects() =
        projectsStorage.getAll().ifEmpty { throw NotFoundException("projects") }

    override fun addMateToProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in project.matesIds) throw AlreadyExistException()
            projectsStorage.update(project.copy(matesIds = project.matesIds + mateId))
        } ?: throw NotFoundException("project")
    }

    override fun updateProject(updatedProject: Project) = authSafeCall { currentUser ->
        if (updatedProject.createdBy == currentUser.id) throw AccessDeniedException()
        projectsStorage.update(updatedProject)
    }

    override fun addStateToProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (state in project.states) throw AlreadyExistException()
            projectsStorage.update(project.copy(states = project.states + state))
        } ?: throw NotFoundException("project")
    }

    override fun addProject(name: String) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        projectsStorage.add(
            Project(
                name = name,
                createdBy = currentUser.id,
            )
        )
    }

    override fun editProjectName(projectId: UUID, name: String) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsStorage.update(project.copy(name = name))
        } ?: throw NotFoundException("project")
    }

    override fun deleteMateFromProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val mates = project.matesIds.toMutableList()
            mates.removeIf { it == mateId }
            projectsStorage.update(project.copy(matesIds = mates))
        } ?: throw NotFoundException("project")
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsStorage.delete(project)
        } ?: throw NotFoundException("project")
    }

    override fun deleteStateFromProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsStorage.findByProjectId(projectId)?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val states = project.states.toMutableList()
            states.removeIf { it == state }
            projectsStorage.update(project.copy(states = states))
        } ?: throw NotFoundException("project")
    }
}