package org.example.domain.usecase.task

import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class GetTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(taskId: UUID): Task {


        val currentUser = authenticationRepository.getCurrentUser()
            .getOrElse { throw UnauthorizedException(
                "You are not authorized to perform this action. Please log in again."
            ) }

        val task = tasksRepository.getTaskById(taskId).getOrElse {
            throw NotFoundException(
                "The task with ID $taskId was not found. Please check the ID and try again."
            )
        }

        if (!isAuthorized(currentUser, task)) {
            throw UnauthorizedException(
                "You are not authorized to view this task. Please contact your project manager."
            )
        }

        return task
    }


    private fun isAuthorized(user: User, task: Task): Boolean {
        return user.type == UserType.ADMIN ||
                task.createdBy == user.id ||
                task.assignedTo.contains(user.id)
    }

}
