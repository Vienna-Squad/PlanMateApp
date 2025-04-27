package org.example.domain.usecase.task

import org.example.domain.entity.Log
import org.example.domain.repository.TasksRepository

class GetTaskHistoryUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String): List<Log> {
        return emptyList()
    }
}