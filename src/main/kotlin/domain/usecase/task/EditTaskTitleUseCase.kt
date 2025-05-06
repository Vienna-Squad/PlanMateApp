package org.example.domain.usecase.task

import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskTitleUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(taskId: UUID, newTitle: String) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(title = newTitle))
        logsRepository.addLog(
            ChangedLog(
                affectedId = task.toString(),
                affectedType = Log.AffectedType.TASK,
                changedFrom = task.title,
                changedTo = newTitle
            )
        )
    }
}
