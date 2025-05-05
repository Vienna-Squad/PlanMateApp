package org.example.data.repository

import org.example.data.datasource.csv.ProjectsCsvStorage
import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.UserRole
import org.example.domain.repository.ProjectsRepository
import org.example.domain.AlreadyExistException
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsCsvStorage: ProjectsCsvStorage,
) : Repository(), ProjectsRepository {
    override fun getProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            project
        } ?: throw NotFoundException("project")
    }

    override fun getAllProjects() =
        safeCall { projectsCsvStorage.read().ifEmpty { throw NotFoundException("projects") } }

    override fun addMateToProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in project.matesIds) throw AlreadyExistException()
            projectsCsvStorage.updateItem(project.copy(matesIds = project.matesIds + mateId))
        } ?: throw NotFoundException("project")
    }

    override fun addStateToProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            if (state in project.states) throw AlreadyExistException()
            projectsCsvStorage.updateItem(project.copy(states = project.states + state))
        } ?: throw NotFoundException("project")
    }

    override fun addProject(name: String) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        projectsCsvStorage.append(
            Project(
                name = name,
                createdBy = currentUser.id,
            )
        )
    }

    override fun editProjectName(projectId: UUID, name: String) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsCsvStorage.updateItem(project.copy(name = name))
        } ?: throw NotFoundException("project")
    }

    override fun deleteMateFromProject(projectId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val mates = project.matesIds.toMutableList()
            mates.removeIf { it == mateId }
            projectsCsvStorage.updateItem(project.copy(matesIds = mates))
        } ?: throw NotFoundException("project")
    }

    override fun deleteProjectById(projectId: UUID) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            projectsCsvStorage.deleteItem(project)
        } ?: throw NotFoundException("project")
    }

    override fun deleteStateFromProject(projectId: UUID, state: String) = authSafeCall { currentUser ->
        projectsCsvStorage.read().find { it.id == projectId }?.let { project ->
            if (project.createdBy != currentUser.id) throw AccessDeniedException()
            val states = project.states.toMutableList()
            states.removeIf { it == state }
            projectsCsvStorage.updateItem(project.copy(states = states))
        } ?: throw NotFoundException("project")
    }
}
