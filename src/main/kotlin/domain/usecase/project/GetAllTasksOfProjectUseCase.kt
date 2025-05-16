package org.example.domain.usecase.project

import org.example.domain.NoTasksFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(projectId: UUID): List<Task> {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        validator.canGetAllTasksOfProject(project, currentUser)
        return tasksRepository.getAllTasks()
            .filter { task -> task.projectId == projectId }
            .ifEmpty { throw NoTasksFoundException() }
    }
}