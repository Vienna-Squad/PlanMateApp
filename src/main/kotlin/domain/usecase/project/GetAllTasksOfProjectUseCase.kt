package org.example.domain.usecase.project

import org.example.domain.InvalidIdException
import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class GetAllTasksOfProjectUseCase(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository

) {
    operator fun invoke(projectId: String): List<Task> {

        val currentUser = authenticationRepository.getCurrentUser().getOrElse {
            throw UnauthorizedException()
        }

        val project = projectsRepository.get(projectId).getOrElse {
            throw InvalidIdException()
        }

        if (currentUser.type != UserType.ADMIN &&
            currentUser.id != project.createdBy &&
            currentUser.id !in project.matesIds) {
            throw UnauthorizedException()
        }

        val allTasks = tasksRepository.getAll().getOrElse {
            throw NoFoundException()
        }


        return allTasks
            .filter { it.projectId == project.id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoFoundException()
    }


}