package org.example.domain.usecase.task

import org.example.domain.exceptions.NoChangeException
import org.example.domain.exceptions.TaskAccessDeniedException
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class EditTaskTitleUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(taskId: UUID, newTitle: String) =
        usersRepository.getCurrentUser().let { currentUser ->
            tasksRepository.getTaskById(taskId).let { task ->
                projectsRepository.getProjectById(task.projectId).let { project ->
                    if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw TaskAccessDeniedException()
                    if (task.title == newTitle) throw NoChangeException()
                    tasksRepository.updateTask(task.copy(title = newTitle))
                    logsRepository.addLog(
                        ChangedLog(
                            username = currentUser.username,
                            affectedId = task.id,
                            affectedName = task.title,
                            affectedType = Log.AffectedType.TASK,
                            changedFrom = task.title,
                            changedTo = newTitle
                        )
                    )
                }
            }
        }
}
