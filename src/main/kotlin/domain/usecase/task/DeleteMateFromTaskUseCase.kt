package org.example.domain.usecase.task
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository

class DeleteMateFromTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logRepository: LogsRepository
) {
    operator fun invoke(taskId: String, mate: String) {}
}