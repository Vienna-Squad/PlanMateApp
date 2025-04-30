package org.example.domain.usecase.project

import org.example.domain.NoFoundException
import org.example.domain.NoProjectFoundException
import org.example.domain.UnauthorizedException
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
                .getOrElse { throw NoProjectFoundException() }.let { project ->

                    when (currentUser.type) {
                        UserType.ADMIN -> {
                            if (project.createdBy != currentUser.id) {
                                throw UnauthorizedException()
                            }
                        }

                        UserType.MATE -> {
                            if (!project.matesIds.contains(currentUser.id)) {
                                throw UnauthorizedException()
                            }
                        }
                    }
                }
        }
        return logsRepository.getAll().getOrElse { throw NoFoundException() }.filter { logs ->
            logs.id == projectId
        }

    }
}
