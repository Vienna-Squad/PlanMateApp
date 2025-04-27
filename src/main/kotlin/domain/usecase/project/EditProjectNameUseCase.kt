package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository

class EditProjectNameUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String, name: String) {}
}