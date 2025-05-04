package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID) = tasksRepository.deleteTaskById(taskId)
}
