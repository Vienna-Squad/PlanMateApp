package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class EditProjectStatesUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(projectId: UUID, states: List<String>) {
        doIfAuthorized(authenticationRepository::getCurrentUser) { user ->
            if (user.type == UserType.MATE) throw AccessDeniedException()
            doIfExistedProject(projectId, projectsRepository::getProjectById) { project ->
                if (project.createdBy != user.id) throw AccessDeniedException()
                val isSameStates = project.states.containsAll(states) && states.containsAll(project.states)
                if (isSameStates) {
                    throw InvalidIdException();
                } else {
                    projectsRepository.updateProject(project.copy(states = states))
                    logsRepository.addLog(
                        ChangedLog(
                            username = user.username,
                            affectedId = projectId,
                            affectedType = Log.AffectedType.PROJECT,
                            changedFrom = project.states.toString(),
                            changedTo = states.toString(),
                        )
                    )
                }

            }
        }
    }

    private fun doIfAuthorized(getCurrentUser: () -> Result<User>, block: (User) -> Unit) {
        block(getCurrentUser().getOrElse { throw UnauthorizedException() })
    }

    private fun doIfExistedProject(
        projectId: UUID,
        getProject: (UUID) -> Result<Project>,
        block: (Project) -> Unit
    ) {
        block(getProject(projectId).getOrElse { throw if (projectId.toString().isBlank()) InvalidIdException() else NoFoundException() })
    }
}