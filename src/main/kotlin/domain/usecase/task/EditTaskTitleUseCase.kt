package org.example.domain.usecase.task

import org.example.domain.NoFoundException
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
        authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException() }.let { user ->
            tasksRepository.getAllTasks().getOrElse {  throw NoFoundException() }
                .filter { task -> task.id == taskId }
                .also { tasks -> if (tasks.isEmpty())  throw NoFoundException() }
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
                    ).getOrElse { throw NoFoundException() }
                }
                .copy(title = title)
                .let { task ->
                    tasksRepository.updateTask(task).getOrElse { throw NoFoundException() }
                }
        }
    }

}