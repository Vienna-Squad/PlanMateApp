package org.example.domain.usecase.task

import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
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
            .getOrElse { throw UnauthorizedException() }

        val task = tasksRepository.getTaskById(taskId).getOrElse {
            throw NoFoundException()
        }

        if (!isAuthorized(currentUser, task)) {
            throw UnauthorizedException()
        }

        return task
    }


    private fun isAuthorized(user: User, task: Task): Boolean {
        return user.type == UserType.ADMIN ||
                task.createdBy == user.id ||
                task.assignedTo.contains(user.id)
    }

}
