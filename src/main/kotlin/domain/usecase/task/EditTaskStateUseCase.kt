package org.example.domain.usecase.task

import org.example.domain.NoChangeException
import org.example.domain.ProjectAccessDeniedException
import org.example.domain.ProjectHasNoThisStateException
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class EditTaskStateUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(taskId: UUID, stateName: String) =
        usersRepository.getCurrentUser().let { currentUser ->
            tasksRepository.getTaskById(taskId).let { task ->
                projectsRepository.getProjectById(task.projectId).let { project ->
                    if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds)
                        throw ProjectAccessDeniedException()
                    if (task.state.name == stateName) throw NoChangeException()
                    projectsRepository.getProjectById(task.projectId).states.find { it.name == stateName }
                        ?.let { state ->
                            tasksRepository.updateTask(task.copy(state = state))
                            logsRepository.addLog(
                                ChangedLog(
                                    username = currentUser.username,
                                    affectedId = task.id,
                                    affectedName = task.title,
                                    affectedType = Log.AffectedType.TASK,
                                    changedFrom = task.state.name,
                                    changedTo = stateName
                                )
                            )
                        } ?: throw ProjectHasNoThisStateException()
                }
            }
        }
}