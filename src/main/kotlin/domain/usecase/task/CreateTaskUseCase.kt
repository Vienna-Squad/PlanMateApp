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
                throw UnauthorizedException()
            }.also { currentUser ->
                projectsRepository.get(newTask.projectId)
                    .getOrElse {
                        throw NoFoundException()
                    }.also { project ->

                if (!project.matesIds.contains(currentUser.id)
                    &&(project.createdBy != currentUser.id)){throw AccessDeniedException()}

                tasksRepository.add(newTask).getOrElse {throw FailedToAddException() }
                logsRepository.add(
                    CreatedLog(
                        username = currentUser.username,
                        affectedId = newTask.id,
                        affectedType = Log.AffectedType.TASK,
                    )
                ).getOrElse { throw FailedToLogException() }
            }
    }
}
}