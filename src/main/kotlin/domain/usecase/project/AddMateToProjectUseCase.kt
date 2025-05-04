package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import java.util.*

class AddMateToProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID, mateId: UUID) =
        projectsRepository.addMateToProject(projectId = projectId, mateId = mateId)
}