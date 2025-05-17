package org.example.domain.usecase.task

import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class DeleteMateFromTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
    private val validator: Validator
) {
    operator fun invoke(taskId: UUID, mateId: UUID) {
        val currentUser = usersRepository.getCurrentUser()
        val task = tasksRepository.getTaskById(taskId)
        val project = projectsRepository.getProjectById(task.projectId)
        validator.canDeleteMateFromTask(project, task, currentUser, mateId)
        tasksRepository.updateTask(task.copy(assignedTo = task.assignedTo - mateId))
        logsRepository.addLog(DeletedLog(
                username = currentUser.username,
                affectedId = mateId,
                affectedName = usersRepository.getUserByID(mateId).username,
                affectedType = Log.AffectedType.MATE,
                deletedFrom = "task ${task.title} [$taskId]"
            ))
    }
}