package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class CreateTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(title: String, state: String, projectId: UUID) = tasksRepository.addTask(
        title = title,
        state = state,
        projectId = projectId
    )
}