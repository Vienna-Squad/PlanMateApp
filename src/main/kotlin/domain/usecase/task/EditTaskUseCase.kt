package org.example.domain.usecase.task

import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class EditTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String, updatedTask: Task) {}
}