package org.example.domain.usecase.project


import org.example.domain.repository.ProjectsRepository


class AddStateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(projectId: String, state: String) {}
}
