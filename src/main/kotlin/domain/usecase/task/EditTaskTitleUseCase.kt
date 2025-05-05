package org.example.domain.usecase.task

import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Log.AffectedType
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskTitleUseCase(
    private val authRepository: AuthRepository,
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(taskId: UUID, title: String) = tasksRepository.getTaskById(taskId)
        .let { task ->
            tasksRepository.editTask(
                taskId = taskId,
                updatedTask = task.copy(title = title),
            )
            task.title
        }.let { taskTitle ->
            val user = authRepository.getCurrentUser() ?: throw UnauthorizedException()
            logsRepository.addLog(
                ChangedLog(
                    username = user.username,
                    affectedId = taskId,
                    affectedType = AffectedType.TASK,
                    changedFrom = taskTitle,
                    changedTo = title
                )
            )
        }
}
