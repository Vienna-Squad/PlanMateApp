package org.example.domain.usecase.task

import org.example.domain.AlreadyExistException
import org.example.domain.ProjectHasNoException
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
        tasksRepository.getTaskById(taskId).let { task ->
            if (task.assignedTo.contains(mateId)) throw AlreadyExistException("mate")
            projectsRepository.getProjectById(task.projectId).let { project ->
                if (!project.matesIds.contains(mateId)) throw ProjectHasNoException("mate")
                tasksRepository.updateTask(task.copy(assignedTo = task.assignedTo + mateId))
                logsRepository.addLog(
                    AddedLog(
                        username = usersRepository.getCurrentUser().username,
                        affectedId = mateId,
                        affectedName = usersRepository.getUserByID(mateId).username,
                        affectedType = Log.AffectedType.MATE,
                        addedTo = "task ${task.title} [$taskId]"
                    )
                )
            }
        }
}