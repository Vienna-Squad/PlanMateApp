package org.example.domain.usecase.task

import org.example.domain.AccessDeniedException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(taskId: UUID) =
        usersRepository.getCurrentUser().let { currentUser ->
            tasksRepository.getTaskById(taskId).let { task ->
                projectsRepository.getProjectById(task.projectId).let { project ->
                    if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw AccessDeniedException(

                    )
                    tasksRepository.deleteTaskById(taskId)
                    logsRepository.addLog(
                        DeletedLog(
                            username = currentUser.username,
                            affectedId = taskId,
                            affectedName = task.title,
                            affectedType = Log.AffectedType.TASK,
                        )
                    )
                }
            }
        }
}