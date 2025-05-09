package org.example.domain.usecase.task

import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(taskId: UUID) =
        tasksRepository.getTaskById(taskId).let { task ->
            tasksRepository.deleteTaskById(taskId)
            logsRepository.addLog(
                DeletedLog(
                    username = usersRepository.getCurrentUser().username,
                    affectedId = taskId,
                    affectedName = task.title,
                    affectedType = Log.AffectedType.TASK,
                )
            )
        }
}