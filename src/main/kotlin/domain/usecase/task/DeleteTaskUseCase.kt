package org.example.domain.usecase.task

import org.example.domain.FailedToAddException
import org.example.domain.repository.TasksRepository

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String) {
        tasksRepository.delete(taskId)
            .getOrElse { throw FailedToAddException() }
    }
}
