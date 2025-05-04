package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository


class CreateProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(name: String) = projectsRepository.addProject(name)
}