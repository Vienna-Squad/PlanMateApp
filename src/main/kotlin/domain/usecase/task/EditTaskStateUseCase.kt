package org.example.domain.usecase.task

import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class EditTaskStateUseCase(
    private val tasksRepository: TasksRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
    private val validator: Validator
) {
    operator fun invoke(taskId: UUID, stateName: String) {
        val currentUser = usersRepository.getCurrentUser()
        val task = tasksRepository.getTaskById(taskId)
        val project = projectsRepository.getProjectById(task.projectId)
        validator.canEditTaskState(project, task, currentUser, stateName)
        val state = validator.getStateIfExistInProject(project, stateName)
        tasksRepository.updateTask(task.copy(state = state))
        logsRepository.addLog(ChangedLog(
                username = currentUser.username,
                affectedId = task.id,
                affectedName = task.title,
                affectedType = Log.AffectedType.TASK,
                changedFrom = task.state.name,
                changedTo = stateName
            ))
    }
}