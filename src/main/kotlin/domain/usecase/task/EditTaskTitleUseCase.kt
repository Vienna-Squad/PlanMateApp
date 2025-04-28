package org.example.domain.usecase.task

import org.example.domain.NoTaskFoundException
import org.example.domain.repository.TasksRepository

class EditTaskTitleUseCase (
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(taskId: String, title: String) {
         // get Tasks from tasksRepo
         // check if task is found
        return throw NoTaskFoundException("")
    }
}