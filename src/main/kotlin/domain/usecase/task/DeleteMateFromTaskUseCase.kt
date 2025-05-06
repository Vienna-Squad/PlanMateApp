package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteMateFromTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, mateId: UUID) = tasksRepository.getTaskById(taskId).let { task ->
        task.assignedTo.toMutableList().apply {
            remove(mateId)
            tasksRepository.updateTask(task.copy(assignedTo = this))
        }
    }
}