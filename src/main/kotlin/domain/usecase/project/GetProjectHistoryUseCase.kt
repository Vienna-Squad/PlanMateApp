package org.example.domain.usecase.project

import org.example.domain.NoLogsFoundException
import org.example.domain.NoProjectFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Log
import org.example.domain.entity.User
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

        val currentUser= authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException() }

        if(currentUser.type!=UserType.ADMIN){
            throw UnauthorizedException()
        }
        projectsRepository.get(projectId)
            .getOrElse { throw NoProjectFoundException() }

        return logsRepository.getAll().getOrElse { throw NoLogsFoundException() }.filter {logs->
            logs.id==projectId
        }

    }
}