package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskStateUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, state: String) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.editTask(
            taskId = taskId,
            updatedTask = task.copy(state = state),
        )
    }
}