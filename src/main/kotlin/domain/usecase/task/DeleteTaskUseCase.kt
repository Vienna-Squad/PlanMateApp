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

class DeleteTaskUseCase(
    private val projectsRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(taskId: String) {
        doIfAuthorized(authenticationRepository::getCurrentUser) { user ->
            if (user.type == UserType.MATE) throw AccessDeniedException()
            doIfExistedProject(taskId, projectsRepository::get) { project ->
                if (project.createdBy != user.id) throw AccessDeniedException()
                doIfExistedTask(taskId, tasksRepository::get) { task ->
                    if (task.projectId != project.id) throw AccessDeniedException()
                    tasksRepository.delete(task.id)
                    logsRepository.add(
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
        projectId: String,
        getProject: (String) -> Result<Project>,
        block: (Project) -> Unit
    ) {
        block(getProject(projectId).getOrElse { throw if (projectId.isBlank()) InvalidIdException() else NoFoundException() })
    }

    private fun doIfExistedTask(
        taskId: String,
        getTask: (String) -> Result<Task>,
        block: (Task) -> Unit
    ) {
        block(getTask(taskId).getOrElse { throw if (taskId.isBlank()) InvalidIdException() else NoFoundException() })
    }
}