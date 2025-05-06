package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import java.util.*

class AddMateToProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID, mateId: UUID) = projectsRepository.getProjectById(projectId).let { project ->
        projectsRepository.updateProject(project.copy(matesIds = project.matesIds + mateId))
    }
}