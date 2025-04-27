package org.example.domain.usecase.task

import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class EditTaskTitleUseCase (
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String, title: String) {}
}