package org.example.domain.usecase.project

import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(projectId: UUID) = projectsRepository.deleteProjectById(projectId).also {
        logsRepository.addLog(
            DeletedLog(
                affectedId = projectId.toString(),
                affectedType = Log.AffectedType.PROJECT,
            )
        )
    }
}