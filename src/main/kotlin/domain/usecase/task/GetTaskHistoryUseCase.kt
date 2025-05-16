package org.example.domain.usecase.task

import org.example.domain.LogsNotFoundException
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class GetTaskHistoryUseCase(
    private val logsRepository: LogsRepository,
    private val projectsRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(taskId: UUID): List<Log> {
        val currentUser = usersRepository.getCurrentUser()
        val task = tasksRepository.getTaskById(taskId)
        val project = projectsRepository.getProjectById(task.projectId)
        validator.canGetTaskHistory(project, currentUser)
        return logsRepository.getAllLogs()
            .filter { it.toString().contains(taskId.toString()) }
            .ifEmpty { throw LogsNotFoundException() }
    }
}
