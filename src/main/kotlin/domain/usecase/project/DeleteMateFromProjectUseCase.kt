package org.example.domain.usecase.project

import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class DeleteMateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) = projectsRepository.getProjectById(projectId).let { project ->
        project.matesIds.toMutableList().let { matesIds ->
            matesIds.remove(mateId)
            projectsRepository.updateProject(project.copy(matesIds = matesIds))
            logsRepository.addLog(
                DeletedLog(
                    affectedId = mateId.toString(),
                    affectedType = Log.AffectedType.MATE,
                    deletedFrom = "project $projectId"
                )
            )
        }
    }
}