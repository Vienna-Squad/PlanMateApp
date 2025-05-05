package org.example.domain.usecase.project

import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) =
        projectsRepository.addMateToProject(projectId = projectId, mateId = mateId).also {
            authRepository.getCurrentUser()?.let { currentUser ->
                logsRepository.addLog(
                    AddedLog(
                        username = currentUser.username,
                        affectedId = mateId,
                        affectedType = Log.AffectedType.MATE,
                        addedTo = projectId
                    )
                )
            }
        }
}