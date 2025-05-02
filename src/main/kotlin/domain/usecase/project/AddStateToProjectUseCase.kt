package org.example.domain.usecase.project


import org.example.domain.*
import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDateTime


class AddStateToProjectUseCase(
    private val authenticationRepository: AuthenticationRepository = getKoin().get(),
    private val projectsRepository: ProjectsRepository = getKoin().get(),
    private val logsRepository: LogsRepository = getKoin().get()
) {


    operator fun invoke(projectId: String, state: String) {
        authenticationRepository
            .getCurrentUser()
            .getOrElse {
                throw UnauthorizedException()
            }.also { currentUser ->
                if (currentUser.type != UserType.ADMIN) {
                    throw AccessDeniedException()
                }
                projectsRepository.get(projectId)
                    .getOrElse {
                        throw NoFoundException()
                    }
                    .also { project ->
                        if (project.createdBy != currentUser.id) throw AccessDeniedException()
                        if (project.states.contains(state)) throw AlreadyExistException()
                        projectsRepository.update(
                            project.copy(
                                states = project.states + state
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
                ).getOrElse { throw FailedToLogException() }
            }

    }
}


