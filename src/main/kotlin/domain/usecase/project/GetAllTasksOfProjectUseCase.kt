package org.example.domain.usecase.project

import org.example.domain.entity.Task

class GetAllTasksOfProjectUseCase {
    operator fun invoke(projectId: String): List<Task> {
        return emptyList()
    }
}