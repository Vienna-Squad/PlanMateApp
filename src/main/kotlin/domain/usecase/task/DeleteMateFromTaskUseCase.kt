package org.example.domain.usecase.task

import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteMateFromTaskUseCase(private val tasksRepository: TasksRepository) {
    operator fun invoke(taskId: UUID, mateId: UUID) = tasksRepository.deleteMateFromTask(
        taskId = taskId,
        mateId = mateId
    )
}