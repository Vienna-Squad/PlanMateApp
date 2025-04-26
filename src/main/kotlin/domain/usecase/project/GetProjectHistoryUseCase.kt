package org.example.domain.usecase.project

import org.example.domain.entity.Log

class GetProjectHistoryUseCase {
    operator fun invoke(projectId: String): List<Log> {
        return emptyList()
    }
}