package org.example.domain.usecase.task

import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class AddMateToTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(taskId: UUID, mateId: UUID) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(assignedTo = task.assignedTo + mateId))
        logsRepository.addLog(
            AddedLog(
                username = usersRepository.getCurrentUser().username,
                affectedId = mateId.toString(),
                affectedType = Log.AffectedType.MATE,
                addedTo = "task $taskId"
            )
        )
    }
}