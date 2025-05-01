package org.example.domain.usecase.project


import org.example.domain.AccessDeniedException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.time.LocalDateTime


class AddStateToProjectUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(projectId: String, state: String) {
        val currentUser = authenticationRepository
            .getCurrentUser()
            .getOrElse {
                throw UnauthorizedException()
            }.also {
                if (it.type != UserType.ADMIN) {
                    throw AccessDeniedException()
                }
            }
        projectsRepository.get(projectId)
            .getOrElse {
                throw NoFoundException()
            }
            .also {
                projectsRepository.update(
                        it.copy(
                            states = it.states + state
                        )
                    )
            }
        logsRepository.add(
            AddedLog(
                username = currentUser.username,
                affectedId = state,
                affectedType = Log.AffectedType.STATE,
                dateTime = LocalDateTime.now(),
                addedTo = projectId,
            )
        )
    }
}


