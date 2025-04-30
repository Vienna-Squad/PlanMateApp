package org.example.domain.usecase.project

import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.NoFoundException

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String): List<Task> {
        val project = projectsRepository.get(projectId).getOrElse {
            throw NoFoundException()
        }

        val allTasks = tasksRepository.getAll().getOrElse {
            throw NoFoundException()
        }

        return allTasks
            .filter { it.projectId == project.id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoFoundException()
    }
}