package org.example.domain.usecase.task


import org.example.domain.MateAlreadyExistsException
import org.example.domain.ProjectHasNoThisMate
import org.example.domain.ProjectAccessDenied
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class AddMateToTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(taskId: UUID, mateId: UUID) =
        usersRepository.getCurrentUser().let { currentUser ->
            tasksRepository.getTaskById(taskId).let { task ->
                projectsRepository.getProjectById(task.projectId).let { project ->
                    if (project.createdBy != currentUser.id && currentUser.id !in project.matesIds) throw ProjectAccessDenied()
                    if (task.assignedTo.contains(mateId)) throw MateAlreadyExistsException()
                    if (!project.matesIds.contains(mateId)) throw ProjectHasNoThisMate()
                    tasksRepository.updateTask(task.copy(assignedTo = task.assignedTo + mateId))
                    logsRepository.addLog(
                        AddedLog(
                            username = currentUser.username,
                            affectedId = mateId,
                            affectedName = usersRepository.getUserByID(mateId).username,
                            affectedType = Log.AffectedType.MATE,
                            addedTo = "task ${task.title} [$taskId]"
                        )
                    )
                }
            }
        }
}