package org.example.domain.usecase.task

import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import java.util.*

class DeleteTaskUseCase(
    private val projectsRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(taskId: UUID) {
        doIfAuthorized(authenticationRepository::getCurrentUser) { user ->
            if (user.type == UserType.MATE) throw AccessDeniedException()
            doIfExistedProject(taskId, projectsRepository::getProjectById) { project ->
                if (project.createdBy != user.id) throw AccessDeniedException()
                doIfExistedTask(taskId, tasksRepository::getTaskById) { task ->
                    if (task.projectId != project.id) throw AccessDeniedException()
                    tasksRepository.deleteTaskById(task.id)
                    logsRepository.addLog(
                        DeletedLog(
                            username = user.username,
                            affectedId = taskId,
                            affectedType = Log.AffectedType.PROJECT,
                        )
                    )
                }
            }
        }
    }

    private fun doIfAuthorized(getCurrentUser: () -> Result<User>, block: (User) -> Unit) {
        block(getCurrentUser().getOrElse { throw UnauthorizedException() })
    }

    private fun doIfExistedProject(
        projectId: UUID,
        getProject: (UUID) -> Result<Project>,
        block: (Project) -> Unit
    ) {
        block(getProject(projectId).getOrElse { throw if (projectId.toString().isBlank()) InvalidIdException() else NoFoundException() })
    }

    private fun doIfExistedTask(
        taskId: UUID,
        getTask: (UUID) -> Result<Task>,
        block: (Task) -> Unit
    ) {
        block(getTask(taskId).getOrElse { throw if (taskId.toString().isBlank()) InvalidIdException() else NoFoundException() })
    }
}