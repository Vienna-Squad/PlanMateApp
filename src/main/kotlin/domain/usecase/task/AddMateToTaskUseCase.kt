package org.example.domain.usecase.task

import org.example.domain.entity.AddedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class AddMateToTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, mateId: UUID) = tasksRepository.addMateToTask(
        taskId = taskId,
        mateId = mateId
    )
}