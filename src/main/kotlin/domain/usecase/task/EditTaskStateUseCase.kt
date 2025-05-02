package org.example.domain.usecase.task

import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class EditTaskStateUseCase (
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String, state: String) {
        tasksRepository.get(taskId).onSuccess { task ->
            if (task.state == state) {
                throw InvalidIdException()
            }
            val updatedTask = task.copy(state = state)
            tasksRepository.update(updatedTask)
        }.onFailure { exception ->
            throw when (exception) {
                is NoFoundException -> NoFoundException()
                is InvalidIdException -> InvalidIdException()
                else -> exception
            }
        }
    }
}