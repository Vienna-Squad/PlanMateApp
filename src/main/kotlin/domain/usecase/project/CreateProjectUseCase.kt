package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import org.koin.java.KoinJavaComponent.getKoin


class CreateProjectUseCase(private val projectsRepository: ProjectsRepository = getKoin().get()) {
    operator fun invoke(name: String) = projectsRepository.addProject(name)
}