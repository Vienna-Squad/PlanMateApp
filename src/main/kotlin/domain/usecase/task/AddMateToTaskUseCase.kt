package org.example.domain.usecase.task

import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class AddMateToTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(taskId: UUID, mateId: UUID) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(assignedTo = task.assignedTo + mateId))
        logsRepository.addLog(
            AddedLog(
                affectedId = mateId.toString(),
                affectedType = Log.AffectedType.MATE,
                addedTo = "task $taskId"
            )
        )
    }
}