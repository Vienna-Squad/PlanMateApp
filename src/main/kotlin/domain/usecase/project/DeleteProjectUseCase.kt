package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String) {}
}