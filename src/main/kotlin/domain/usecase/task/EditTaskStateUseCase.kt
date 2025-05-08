package org.example.domain.usecase.task

import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class EditTaskStateUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository
) {
    operator fun invoke(taskId: UUID, newState: String) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(state = newState))
        logsRepository.addLog(
            ChangedLog(
                username = usersRepository.getCurrentUser().username,
                affectedId = task.toString(),
                affectedType = Log.AffectedType.TASK,
                changedFrom = task.state,
                changedTo = newState
            )
        )
    }
}