package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String, mateId: String) {}
}