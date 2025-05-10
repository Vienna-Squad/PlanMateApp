package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class GetProjectHistoryUseCase(
    private val logsRepository: LogsRepository,
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID): List<Log> {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        if (!isOwnerOrMate(project, currentUser)) throw AccessDeniedException("project")
        return logsRepository.getAllLogs()
            .filter { log -> isProjectRelated(log, projectId) }
            .ifEmpty { throw NotFoundException("logs") }
    }

    private fun isOwnerOrMate(project: Project, currentUser: User) =
        project.createdBy == currentUser.id || currentUser.id in project.matesIds

    private fun isProjectRelated(log: Log, projectId: UUID) =
        log.affectedId == projectId || log.toString().contains(projectId.toString())
}
