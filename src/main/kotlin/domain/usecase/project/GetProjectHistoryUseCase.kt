package org.example.domain.usecase.project

import org.example.domain.*
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class GetProjectHistoryUseCase(
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository

) {
    operator fun invoke(projectId: UUID): List<Log> {

        authenticationRepository.getCurrentUser().getOrElse {
            throw UnauthorizedException(
                "User not found"
            )
        }.let { currentUser ->

            projectsRepository.getProjectById(projectId)
                .getOrElse {
                    throw NotFoundException(
                        "Project with id $projectId not found"
                    )
                }.let { project ->

                    projectsRepository.getProjectById(projectId)
                        .getOrElse {
                            throw NotFoundException(
                                "Project with id $projectId not found"
                            )
                        }.let { project ->

                            when (currentUser.type) {
                                UserType.ADMIN -> {
                                    if (project.createdBy != currentUser.id) {
                                        throw AccessDeniedException(
                                            "Only the creator of the project can access the logs"
                                        )
                                    }
                                }

                                UserType.MATE -> {
                                    if (!project.matesIds.contains(currentUser.id)) {
                                        throw AccessDeniedException(
                                            "Only the mates of the project can access the logs"
                                        )
                                    }
                                }
                            }
                        }
                }
            return logsRepository.getAllLogs().getOrElse {
                throw FailedToCallLogException(
                    "Failed to call logs"
                )
            }.filter { logs ->
                logs.affectedId == projectId
            }

        }
    }
}
