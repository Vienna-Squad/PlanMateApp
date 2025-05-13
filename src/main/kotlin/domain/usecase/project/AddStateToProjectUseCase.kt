package org.example.domain.usecase.project


import org.example.domain.ProjectAccessDenied
import org.example.domain.StateAlreadyExistsException
import org.example.domain.entity.State
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class AddStateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, stateName: String) =
        usersRepository.getCurrentUser().let { currentUser ->
            projectsRepository.getProjectById(projectId).let { project ->
                if (project.createdBy != currentUser.id) throw ProjectAccessDenied()
                if (project.states.any { it.name == stateName }) throw StateAlreadyExistsException()
                State(name = stateName).let { stateObj ->
                    projectsRepository.updateProject(project.copy(states = project.states + stateObj))
                    logsRepository.addLog(AddedLog(
                            username = currentUser.username,
                            affectedId = stateObj.id,
                            affectedName = stateName,
                            affectedType = Log.AffectedType.STATE,
                            addedTo = "project ${project.name} [$projectId]"
                        ))
                }
            }
        }
}