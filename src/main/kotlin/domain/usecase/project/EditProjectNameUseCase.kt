package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import java.util.*

class EditProjectNameUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID, newName: String) =
        projectsRepository.getProjectById(projectId).let { project ->
            projectsRepository.updateProject(project.copy(name = newName))
        }
}