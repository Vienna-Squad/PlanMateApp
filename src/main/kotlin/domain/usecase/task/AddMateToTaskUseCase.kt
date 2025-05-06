package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class AddMateToTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, mateId: UUID) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(assignedTo = task.assignedTo + mateId))
    }
}