package org.example.domain.usecase.task

import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository

class EditTaskTitleUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(taskId: String, title: String) {
        authenticationRepository.getCurrentUser().getOrElse { return throw UnauthorizedException() }.let { user ->
            tasksRepository.getAll().getOrElse { return throw NoFoundException() }
                .filter { task -> task.id == taskId }
                .also { tasks -> if (tasks.isEmpty()) return throw NoFoundException() }
                .first()
                .also { task ->
                    logsRepository.add(
                        ChangedLog(
                            username = user.username,
                            affectedId = taskId,
                            affectedType = Log.AffectedType.TASK,
                            changedFrom = task.title,
                            changedTo = title,
                        )
                    ).getOrElse { throw NoFoundException() }
                }
                .copy(title = title)
                .let { task ->
                    tasksRepository.update(task).getOrElse { throw NoFoundException() }
                }
        }
    }

}