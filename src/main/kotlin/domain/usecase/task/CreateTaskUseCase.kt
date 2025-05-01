package org.example.domain.usecase.task

import org.example.domain.UnauthorizedException
import org.example.domain.entity.CreatedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Task
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository

class CreateTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(newTask: Task){
        val currentUser= authenticationRepository.getCurrentUser().getOrElse {
            throw UnauthorizedException()
        }
        tasksRepository.add(newTask)
        logsRepository.add(
            CreatedLog(
                username = currentUser.username,
                affectedId = newTask.id,
                affectedType = Log.AffectedType.TASK,
            )
        )
    }
}