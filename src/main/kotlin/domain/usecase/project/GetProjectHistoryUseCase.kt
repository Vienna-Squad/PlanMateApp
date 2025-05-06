package org.example.domain.usecase.project

import org.example.domain.repository.LogsRepository
import java.util.*

class GetProjectHistoryUseCase(
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(projectId: UUID) = logsRepository.getAllLogs()
        .filter { it.affectedId == projectId.toString() || it.toString().contains(projectId.toString()) }
}
