package org.example.domain.usecase.task

import org.example.domain.entity.AddedLog
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

    }
}