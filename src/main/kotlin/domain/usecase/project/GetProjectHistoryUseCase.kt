package org.example.domain.usecase.project

import org.example.domain.entity.Log
import org.example.domain.repository.ProjectsRepository

class GetProjectHistoryUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String): List<Log> {
        return emptyList()
    }
}