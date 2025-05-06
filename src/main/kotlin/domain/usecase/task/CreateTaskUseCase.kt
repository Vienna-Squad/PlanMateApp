package org.example.domain.usecase.task

import org.example.domain.entity.CreatedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Task
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class CreateTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val usersRepository: UsersRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(title: String, state: String, projectId: UUID) =
        usersRepository.getCurrentUser()?.let { currentUser ->
            Task(
                title = title,
                state = state,
                projectId = projectId,
                createdBy = currentUser.id,
            ).let { newTask ->
                tasksRepository.addTask(newTask)
                logsRepository.addLog(
                    CreatedLog(
                        affectedId = newTask.id.toString(),
                        affectedType = Log.AffectedType.TASK,
                    )
                )
            }
        }


}