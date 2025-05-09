package org.example.domain.usecase.task

import org.example.domain.NoChangeException
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class EditTaskTitleUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(taskId: UUID, newTitle: String) =
        tasksRepository.getTaskById(taskId).let { task ->
            if (task.title == newTitle) throw NoChangeException()
            tasksRepository.updateTask(task.copy(title = newTitle))
            logsRepository.addLog(
                ChangedLog(
                    username = usersRepository.getCurrentUser().username,
                    affectedId = task.id,
                    affectedName = task.title,
                    affectedType = Log.AffectedType.TASK,
                    changedFrom = task.title,
                    changedTo = newTitle
                )
            )
        }
}
