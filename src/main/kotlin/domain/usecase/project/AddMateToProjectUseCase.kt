package org.example.domain.usecase.project

import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(projectId: String, mateId: String, username: String) {

    }
}