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
                throw UnauthorizedException(
                    "You need to be logged in to perform this action"
                )
            }.also { user ->
                if (user.type != UserType.ADMIN) {
                    throw AccessDeniedException(
                        "You need to be an admin to perform this action"
                    )
                }
                projectsRepository.getProjectById(projectId)
                    .getOrElse {
                        throw NotFoundException(
                            "Project with id $projectId not found"
                        )
                    }
                    .also { project ->
                        if (project.createdBy != user.id) throw AccessDeniedException(
                            "You are not the owner of this project"
                        )
                        if (project.matesIds.contains(mateId)) throw AlreadyExistException(
                            "Mate with id $mateId already exists in this project"
                        )
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
                ).getOrElse { throw FailedToLogException(
                    "Failed to add log for adding mate with id $mateId to project with id $projectId"
                ) }
            }
    }
}