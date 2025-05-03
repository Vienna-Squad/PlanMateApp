package org.example.domain.usecase.project

import org.example.domain.InvalidIdException
import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import java.util.*

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository

) {
    operator fun invoke(projectId: UUID): List<Task> {

        val currentUser = authenticationRepository.getCurrentUser().getOrElse {
            throw UnauthorizedException(
                "User not found"
            )
        }

        val project = projectsRepository.getProjectById(projectId).getOrElse {
            throw InvalidIdException(
                "Project ID is invalid"
            )
        }

        if (currentUser.type != UserType.ADMIN &&
            currentUser.id != project.createdBy &&
            currentUser.id !in project.matesIds) {
            throw UnauthorizedException(
                "User is not authorized to access this project"
            )
        }
        val allTasks = tasksRepository.getAllTasks().getOrElse {
            throw NotFoundException(
                "Tasks not found"
            )
        }

        return allTasks.filter { it.projectId == project.id }
            .also { if (it.isEmpty()){
                throw NotFoundException(
                    "No tasks found for project with id $projectId"
                )
            } }
    }
}