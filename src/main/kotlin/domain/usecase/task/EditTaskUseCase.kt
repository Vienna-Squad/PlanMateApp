package org.example.domain.usecase.task

import org.example.domain.entity.Task

class EditTaskUseCase {
    operator fun invoke(taskId: String, updatedTask: Task) {}
}