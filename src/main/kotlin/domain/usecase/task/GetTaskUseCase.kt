package org.example.domain.usecase.task

import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
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

        validateInputs(taskId)

        val userResult = authenticationRepository.getCurrentUser()
        if (userResult.isFailure) {
            throw UnauthorizedException()
        }
        val user = userResult.getOrThrow()
        validateUserAuthorization(user)


        val taskResult = tasksRepository.get(taskId)
        if (taskResult.isFailure) {
            throw NoFoundException()
        }
        return taskResult.getOrThrow()
    }

    private fun validateInputs(taskId: String) {
        require(taskId.isNotBlank()) { throw InvalidIdException() }
    }

    private fun validateUserAuthorization(user: User) {
        require(user.type != UserType.MATE) { throw AccessDeniedException() }
    }
    }
