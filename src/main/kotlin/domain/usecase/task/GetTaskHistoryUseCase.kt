package org.example.domain.usecase.task

import org.example.domain.NoFoundException
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository

class GetTaskHistoryUseCase(
    private val logsRepository: LogsRepository
) {
    operator fun invoke(taskId: String): List<Log> {
        return logsRepository.getAll().getOrNull()?.let {logs->
            logs.filter { it.id==taskId }.takeIf { it.isNotEmpty() } ?:throw NoFoundException()
        } ?: throw NoFoundException()
    }
}