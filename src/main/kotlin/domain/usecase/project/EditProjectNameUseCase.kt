package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import java.util.*

class EditProjectNameUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID, name: String) = projectsRepository.editProjectName(
        projectId = projectId,
        name = name
    )
}