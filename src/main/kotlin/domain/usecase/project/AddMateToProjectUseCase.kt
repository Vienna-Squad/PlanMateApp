package org.example.domain.usecase.project

import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) = projectsRepository.getProjectById(projectId).let { project ->
        projectsRepository.updateProject(project.copy(matesIds = project.matesIds + mateId))
        logsRepository.addLog(
            AddedLog(
                affectedId = mateId.toString(),
                affectedType = Log.AffectedType.MATE,
                addedTo = "project $projectId"
            )
        )
    }
}