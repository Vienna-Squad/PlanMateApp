package org.example.domain.usecase.project

import org.example.domain.NotFoundException
import org.example.domain.repository.TasksRepository
import java.util.*

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
) {
    operator fun invoke(projectId: UUID) = tasksRepository.getAllTasks()
        .filter { task -> task.projectId == projectId }
        .ifEmpty { throw NotFoundException("tasks") }
}