package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository


class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(
        name: String,
        states: List<String>,
        creatorId: String,
        matesIds: List<String> = emptyList()
    ) {
    }
}