package org.example.domain.usecase.task

import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class GetTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
    private val validator: Validator
) {
    operator fun invoke(taskId: UUID): Task {
        val currentUser = usersRepository.getCurrentUser()
        val task = tasksRepository.getTaskById(taskId)
        val project = projectsRepository.getProjectById(task.projectId)
        validator.canGetTask(project, currentUser)
        return task
    }
}