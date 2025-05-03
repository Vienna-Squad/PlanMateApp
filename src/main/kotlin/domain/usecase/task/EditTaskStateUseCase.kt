package org.example.domain.usecase.task

import org.example.domain.InvalidIdException
import org.example.domain.NotFoundException
import org.example.domain.repository.TasksRepository
import java.util.*

class EditTaskStateUseCase (
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: UUID, state: String) {
        tasksRepository.getTaskById(taskId).onSuccess { task ->
            if (task.state == state) {
                throw InvalidIdException(
                    "The task is already in the specified state. Please choose a different state."
                )
            }
            val updatedTask = task.copy(state = state)
            tasksRepository.updateTask(updatedTask)
        }.onFailure { exception ->
            throw when (exception) {
                is NotFoundException -> NotFoundException(
                    "The task with ID $taskId was not found. Please check the ID and try again."
                )
                is InvalidIdException -> InvalidIdException(
                    "The task ID $taskId is invalid. Please check the ID and try again."
                )
                else -> exception
            }
        }
    }
}