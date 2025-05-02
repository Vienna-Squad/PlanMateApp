package org.example.domain.usecase.task

import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.TasksRepository

class GetTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(taskId: String): Task {

        val currentUser = authenticationRepository.getCurrentUser()
            .getOrElse { throw UnauthorizedException() }

        val task = tasksRepository.get(taskId)
            .getOrElse { throw NoFoundException() }

        return if (isAuthorized(currentUser, task)) task else throw UnauthorizedException()
    }


    private fun isAuthorized(user: User, task: Task): Boolean {
        return user.type == UserType.ADMIN ||
                task.createdBy == user.username ||
                task.assignedTo.contains(user.id)
    }

}
