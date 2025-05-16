package org.example.domain.usecase.task

import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class EditTaskTitleUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
    private val validator: Validator
) {
    operator fun invoke(taskId: UUID, newTitle: String) {
        val currentUser = usersRepository.getCurrentUser()
        val task = tasksRepository.getTaskById(taskId)
        val project = projectsRepository.getProjectById(task.projectId)
        validator.canEditTaskTitle(project, task, currentUser, newTitle)
        tasksRepository.updateTask(task.copy(title = newTitle))
        logsRepository.addLog(ChangedLog(
                username = currentUser.username,
                affectedId = task.id,
                affectedName = task.title,
                affectedType = Log.AffectedType.TASK,
                changedFrom = task.title,
                changedTo = newTitle
            ))
    }
}