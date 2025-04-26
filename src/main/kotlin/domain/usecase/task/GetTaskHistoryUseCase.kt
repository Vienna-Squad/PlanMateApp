package org.example.domain.usecase.task

import org.example.domain.entity.Log

class GetTaskHistoryUseCase {
    operator fun invoke(taskId: String): List<Log> {
        return emptyList()
    }
}