package org.example.domain.usecase.project

import org.example.domain.entity.log.Log
import org.example.domain.exceptions.LogsNotFoundException
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class GetProjectHistoryUseCase(
    private val logsRepository: LogsRepository,
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(projectId: UUID): List<Log> {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        validator.canGetProjectHistory(project, currentUser)
        return logsRepository.getAllLogs()
            .filter { log -> log.toString().contains(projectId.toString()) }
            .ifEmpty { throw LogsNotFoundException() }
    }
}
