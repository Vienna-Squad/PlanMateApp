package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String) {}
}