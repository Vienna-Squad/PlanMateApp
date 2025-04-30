package org.example.domain.usecase.project

import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String): List<Task> {
        return emptyList()
    }
}