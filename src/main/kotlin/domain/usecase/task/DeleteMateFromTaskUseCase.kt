package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository

class DeleteMateFromTaskUseCase (
    private val tasksRepository: TasksRepository
){
    operator fun invoke(taskId: String, mate: String) {}
}