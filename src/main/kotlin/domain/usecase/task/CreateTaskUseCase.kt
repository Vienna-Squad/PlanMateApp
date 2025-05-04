package org.example.domain.usecase.task

import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class CreateTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(newTask: Task) = tasksRepository.addTask(newTask)
}