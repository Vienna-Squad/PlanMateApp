package org.example.domain.usecase.task

import org.example.domain.AccessDeniedException
import org.example.domain.FailedToAddLogException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteMateFromTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logRepository: LogsRepository
) {
    operator fun invoke(taskId: UUID, mate: UUID) {

        authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException(
            "User not found"
        ) }.let { currentUser ->

            if (currentUser.type != UserType.ADMIN) {
                throw AccessDeniedException(
                    "Only admins can delete mates from tasks"
                )
            }

            tasksRepository.getTaskById(taskId).getOrElse { throw NotFoundException(
                " Task with id $taskId not found"
            ) }.let { task ->

                if (!task.assignedTo.contains(mate)) {
                    throw NotFoundException(
                        "User with id $mate is not assigned to task $taskId"
                    )
                }

                val updatedAssignedTo = task.assignedTo.filter { it != mate }
                val updatedTask = task.copy(assignedTo = updatedAssignedTo)
                tasksRepository.updateTask(updatedTask)

                logRepository.addLog(
                    log = DeletedLog(
                        username = currentUser.username,
                        affectedType = Log.AffectedType.MATE,
                        affectedId = taskId,
                        deletedFrom = task.title
                    )
                ).getOrElse { throw FailedToAddLogException(
                    "Failed to add log"
                ) }

            }
        }

    }
}