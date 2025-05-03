package org.example.domain.usecase.project

import org.example.domain.*
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.time.LocalDateTime
import java.util.UUID

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(projectId: UUID, mateId: UUID) {
        authenticationRepository
            .getCurrentUser()
            .getOrElse {
                throw UnauthorizedException()
            }.also { user ->
                if (user.type != UserType.ADMIN) {
                    throw AccessDeniedException()
                }
                projectsRepository.getProjectById(projectId)
                    .getOrElse {
                        throw NoFoundException()
                    }
                    .also { project ->
                        if (project.createdBy != user.id) throw AccessDeniedException()
                        if (project.matesIds.contains(mateId)) throw AlreadyExistException()
                        projectsRepository.updateProject(
                            project.copy(
                                matesIds = project.matesIds + mateId
                            )
                        )
                    }

                logsRepository.addLog(
                    AddedLog(
                        username = user.username,
                        affectedId = mateId,
                        affectedType = Log.AffectedType.MATE,
                        dateTime = LocalDateTime.now(),
                        addedTo = projectId,
                    )
                ).getOrElse { throw FailedToLogException() }
            }
    }
}