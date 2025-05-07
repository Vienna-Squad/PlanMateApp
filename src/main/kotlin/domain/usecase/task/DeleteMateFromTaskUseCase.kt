package org.example.domain.usecase.task

import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteMateFromTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(taskId: UUID, mateId: UUID) =
        tasksRepository.getTaskById(taskId).let { task ->
        task.assignedTo.toMutableList().let { mates ->
            mates.remove(mateId)
            tasksRepository.updateTask(task.copy(assignedTo = mates))
            logsRepository.addLog(
                DeletedLog(
                    affectedId = mateId.toString(),
                    affectedType = Log.AffectedType.MATE,
                    deletedFrom = "task $taskId"
                )
            )
        }
    }
}