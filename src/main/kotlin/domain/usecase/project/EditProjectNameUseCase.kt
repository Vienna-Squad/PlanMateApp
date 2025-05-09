package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.NoChangeException
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class EditProjectNameUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, newName: String) =
        usersRepository.getCurrentUser().let { currentUser ->
            projectsRepository.getProjectById(projectId).let { project ->
                if (project.createdBy != currentUser.id) throw AccessDeniedException("project")
                if (project.name == newName) throw NoChangeException()
                projectsRepository.updateProject(project.copy(name = newName))
                logsRepository.addLog(
                    ChangedLog(
                        username = currentUser.username,
                        affectedId = projectId,
                        affectedName = project.name,
                        affectedType = Log.AffectedType.PROJECT,
                        changedFrom = project.name,
                        changedTo = newName
                    )
                )
            }
        }
}