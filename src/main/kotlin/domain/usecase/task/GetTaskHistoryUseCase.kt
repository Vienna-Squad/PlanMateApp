package org.example.domain.usecase.task

import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetTaskHistoryUseCase(
    private val logsRepository: LogsRepository = getKoin().get()
) {
    operator fun invoke(taskId: UUID): Result<List<Log>> {
        return logsRepository.getAllLogs(taskId)
    }
}
