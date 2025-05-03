package org.example.domain.usecase.task

import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskTitleUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(taskId: UUID, title: String) {
        authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException(
            "You are not authorized to perform this action. Please log in again."
        ) }.let { user ->
            tasksRepository.getAllTasks().getOrElse {  throw NotFoundException(
                "No tasks found. Please check the task ID and try again."
            ) }
                .filter { task -> task.id == taskId }
                .also { tasks -> if (tasks.isEmpty())  throw NotFoundException(
                    "The task with ID $taskId was not found. Please check the ID and try again."
                ) }
                .first()
                .also { task ->
                    logsRepository.addLog(
                        ChangedLog(
                            username = user.username,
                            affectedId = taskId,
                            affectedType = Log.AffectedType.TASK,
                            changedFrom = task.title,
                            changedTo = title,
                        )
                    ).getOrElse { throw NotFoundException(
                        "Failed to log the change. Please try again later."
                    ) }
                }
                .copy(title = title)
                .let { task ->
                    tasksRepository.updateTask(task).getOrElse { throw NotFoundException(
                        "Failed to update the task. Please try again later."
                    ) }
                }
        }
    }

}