package org.example.domain.usecase.task

import org.example.domain.AccessDeniedException
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class GetTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(taskId: UUID) =
        usersRepository.getCurrentUser().let { currentUser ->
            tasksRepository.getTaskById(taskId).let { task ->
                projectsRepository.getProjectById(task.projectId).let { project ->
                    if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw AccessDeniedException("task")
                    task
                }
            }
        }
}
