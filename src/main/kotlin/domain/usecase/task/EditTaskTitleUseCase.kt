package org.example.domain.usecase.task

import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class EditTaskTitleUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(taskId: UUID, newTitle: String) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(title = newTitle))
        logsRepository.addLog(
            ChangedLog(
                username = usersRepository.getCurrentUser().username,
                affectedId = task.toString(),
                affectedType = Log.AffectedType.TASK,
                changedFrom = task.title,
                changedTo = newTitle
            )
        )
    }
}
