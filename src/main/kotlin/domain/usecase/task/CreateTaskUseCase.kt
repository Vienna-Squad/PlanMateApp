package org.example.domain.usecase.task

import org.example.domain.*

import org.example.domain.entity.CreatedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Task
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository

class CreateTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(newTask: Task) {
        authenticationRepository.getCurrentUser()
            .getOrElse {
                throw UnauthorizedException(
                    "User not found"
                )
            }.also { currentUser ->
                projectsRepository.getProjectById(newTask.projectId)
                    .getOrElse {
                        throw NotFoundException(
                            "Project with id ${newTask.projectId} not found"
                        )
                    }.also { project ->

                if (!project.matesIds.contains(currentUser.id)
                    &&(project.createdBy != currentUser.id)){throw AccessDeniedException(
                        " Only the mates of the project or creator can create tasks"
                    )}

                tasksRepository.addTask(newTask).getOrElse {throw FailedToAddException(
                    "Failed to add task"
                ) }
                logsRepository.addLog(
                    CreatedLog(
                        username = currentUser.username,
                        affectedId = newTask.id,
                        affectedType = Log.AffectedType.TASK,
                    )
                ).getOrElse { throw FailedToLogException(
                    "Failed to add log"
                ) }
            }
    }
}
}