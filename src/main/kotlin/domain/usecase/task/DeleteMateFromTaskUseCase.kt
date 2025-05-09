package org.example.domain.usecase.task

import org.example.domain.AccessDeniedException
import org.example.domain.TaskHasNoException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteMateFromTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(taskId: UUID, mateId: UUID) =
        usersRepository.getCurrentUser().let { currentUser ->
            tasksRepository.getTaskById(taskId).let { task ->
                projectsRepository.getProjectById(task.projectId).let { project ->
                    if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw AccessDeniedException("task")
                    if (!task.assignedTo.contains(mateId)) throw TaskHasNoException("mate")
                    task.assignedTo.toMutableList().let { mates ->
                        mates.remove(mateId)
                        tasksRepository.updateTask(task.copy(assignedTo = mates))
                        logsRepository.addLog(
                            DeletedLog(
                                username = currentUser.username,
                                affectedId = mateId,
                                affectedName = usersRepository.getUserByID(mateId).username,
                                affectedType = Log.AffectedType.MATE,
                                deletedFrom = "task ${task.title} [$taskId]"
                            )
                        )
                    }
                }
            }
        }
}