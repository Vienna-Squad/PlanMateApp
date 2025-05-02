package org.example.domain.usecase.project

import org.example.domain.*
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository

class GetProjectHistoryUseCase(
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository

) {
    operator fun invoke(projectId: String): List<Log> {

        authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException() }.let { currentUser ->

            projectsRepository.get(projectId)
                .getOrElse { throw NoFoundException() }.let { project ->

                    when (currentUser.type) {
                        UserType.ADMIN -> {
                            if (project.createdBy != currentUser.id) {
                                throw AccessDeniedException()
                            }
                        }

                        UserType.MATE -> {
                            if (!project.matesIds.contains(currentUser.id)) {
                                throw AccessDeniedException()
                            }
                        }
                    }
                }
        }
        return logsRepository.getAll().getOrElse { throw FailedToCallLogException() }.filter { logs ->
            logs.affectedId == projectId
        }

    }
}
