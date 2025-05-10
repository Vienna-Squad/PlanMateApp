package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteMateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        if (project.createdBy != currentUser.id) throw AccessDeniedException("project")
        val mate = usersRepository.getUserByID(mateId)
        if (!project.matesIds.contains(mate.id)) throw ProjectHasNoException("mate")
        val updatedMates = project.matesIds.toMutableList().apply { remove(mateId) }
        projectsRepository.updateProject(project.copy(matesIds = updatedMates))
        logsRepository.addLog(
            DeletedLog(
                username = currentUser.username,
                affectedId = mateId,
                affectedName = mate.username,
                affectedType = Log.AffectedType.MATE,
                deletedFrom = "project ${project.name} [$projectId]"
            )
        )
    }
}