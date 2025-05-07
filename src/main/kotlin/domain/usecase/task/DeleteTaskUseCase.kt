package org.example.domain.usecase.task

import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(taskId: UUID) =
        tasksRepository.deleteTaskById(taskId).let {
        logsRepository.addLog(
            DeletedLog(
                affectedId = taskId.toString(),
                affectedType = Log.AffectedType.TASK,
            )
        )
    }
}
