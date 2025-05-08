package org.example.domain.usecase.project

import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) =
        projectsRepository.getProjectById(projectId).let { project ->
        projectsRepository.updateProject(project.copy(matesIds = project.matesIds + mateId))
        logsRepository.addLog(
            AddedLog(
                username = usersRepository.getCurrentUser().username,
                affectedId = mateId.toString(),
                affectedType = Log.AffectedType.MATE,
                addedTo = "project $projectId"
            )
        )
    }
}