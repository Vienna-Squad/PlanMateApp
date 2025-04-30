package org.example.domain.usecase.task

import org.example.domain.entity.Task
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.TasksRepository

class GetTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(taskId: String): Task {

        TODO("Not yet implemented")
    }
}