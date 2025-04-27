package org.example.domain.usecase.task

import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class EditTaskStateUseCase (
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String, state: String) {}
}