package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteStateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, stateName: String) =
        usersRepository.getCurrentUser().let { currentUser ->
            projectsRepository.getProjectById(projectId).let { project ->
                if (project.createdBy != currentUser.id) throw AccessDeniedException("project")
                project.states.toMutableList().let { states ->
                    states.find { it.name == stateName }?.let { stateObj ->
                        states.remove(stateObj)
                        projectsRepository.updateProject(project.copy(states = states))
                        logsRepository.addLog(
                            DeletedLog(
                                username = currentUser.username,
                                affectedId = stateObj.id,
                                affectedName = stateName,
                                affectedType = Log.AffectedType.STATE,
                                deletedFrom = "project ${project.name} [$projectId]"
                            )
                        )
                    } ?: throw ProjectHasNoException("state")
                }
            }
        }
}
