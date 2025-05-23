package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID): List<Task> {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        if (!isOwnerOrMate(project, currentUser)) throw AccessDeniedException("project")
        return tasksRepository.getAllTasks()
            .filter { task -> task.projectId == projectId }
            .ifEmpty { throw NotFoundException("tasks") }
    }

    private fun isOwnerOrMate(project: Project, currentUser: User) =
        project.createdBy == currentUser.id || currentUser.id in project.matesIds
}