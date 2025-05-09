package org.example.domain.usecase.project

import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID) =
        projectsRepository.getProjectById(projectId).let { project ->
            projectsRepository.deleteProjectById(projectId)
            logsRepository.addLog(
                DeletedLog(
                    username = usersRepository.getCurrentUser().username,
                    affectedId = projectId,
                    affectedName = project.name,
                    affectedType = Log.AffectedType.PROJECT,
                )
            )
        }
}