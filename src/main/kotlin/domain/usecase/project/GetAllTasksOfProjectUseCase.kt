package org.example.domain.usecase.project

import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository

class GetAllTasksOfProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String): List<Task> {
        return emptyList()
    }
}