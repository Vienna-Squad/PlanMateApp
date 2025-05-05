package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskTitleUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, title: String) = tasksRepository.getTaskById(taskId).let { task ->
        tasksRepository.editTask(
            taskId = taskId,
            updatedTask = task.copy(title = title),
        )
    }
}