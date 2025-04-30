package org.example.domain.usecase.task

import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository

class GetTaskHistoryUseCase(
    private val logsRepository: LogsRepository
) {
    operator fun invoke(taskId: String): List<Log> {
        return emptyList()
    }
}