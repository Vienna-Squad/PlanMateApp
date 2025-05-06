package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskStateUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, state: String) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.updateTask(task.copy(state = state))
    }
}