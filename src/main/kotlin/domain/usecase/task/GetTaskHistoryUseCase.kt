package org.example.domain.usecase.task

import org.example.domain.LogsNotFoundException
import org.example.domain.repository.LogsRepository
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetTaskHistoryUseCase(private val logsRepository: LogsRepository = getKoin().get()) {
    operator fun invoke(taskId: UUID) = logsRepository.getAllLogs()
        .filter { it.toString().contains(taskId.toString()) }
        .ifEmpty { throw LogsNotFoundException() }
}
