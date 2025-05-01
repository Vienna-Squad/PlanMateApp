package org.example.domain.usecase.task

import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository

class AddMateToTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(taskId: String, mate: String) {
        if (taskId.isBlank()) {
            throw InvalidIdException()
        }
        if (mate.isBlank()) {
            throw InvalidIdException()
        }

        val currentUser = authenticationRepository.getCurrentUser()
            .getOrElse { throw UnauthorizedException() }

        val task = tasksRepository.get(taskId)
            .getOrElse { throw InvalidIdException() }


        if (currentUser.type != UserType.ADMIN &&
            currentUser.id != task.createdBy &&
            currentUser.id !in task.assignedTo) {
            throw UnauthorizedException()
        }

        authenticationRepository.getUser(mate)
            .getOrElse { throw NoFoundException() }


        val project = projectsRepository.get(task.projectId)
            .getOrElse { throw NoFoundException() }


        if (mate !in project.matesIds) {
            throw NoFoundException()
        }

        val updatedAssignedTo = if (mate !in task.assignedTo) {
            task.assignedTo + mate
        } else {
            task.assignedTo
        }

        val updatedTask = task.copy(assignedTo = updatedAssignedTo)
        tasksRepository.update(updatedTask)
            .getOrElse { throw NoFoundException() }

        val log = AddedLog(
            username = currentUser.username,
            affectedId = mate,
            affectedType = Log.AffectedType.MATE,
            addedTo = taskId
        )
        logsRepository.add(log)
            .getOrElse { throw NoFoundException() }
    }
}