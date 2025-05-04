package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import java.util.*

class DeleteProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID) = projectsRepository.deleteProjectById(projectId)
}