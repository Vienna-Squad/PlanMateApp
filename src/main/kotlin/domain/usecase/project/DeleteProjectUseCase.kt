package org.example.domain.usecase.project

import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID) = projectsRepository.deleteProjectById(projectId).also {
        logsRepository.addLog(
            DeletedLog(
                username = usersRepository.getCurrentUser().username,
                affectedId = projectId.toString(),
                affectedType = Log.AffectedType.PROJECT,
            )
        )
    }
}