package org.example.domain.usecase.task

import org.example.domain.ProjectHasNoException
import org.example.domain.entity.State
import org.example.domain.entity.Task
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class CreateTaskUseCase(
    private val tasksRepository: TasksRepository,
    private val usersRepository: UsersRepository,
    private val logsRepository: LogsRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(title: String, stateName: String, projectId: UUID) =
        projectsRepository.getProjectById(projectId).let { project ->
            if (project.states.all { it.name != stateName }) throw ProjectHasNoException("state")
            usersRepository.getCurrentUser().let { currentUser ->
                Task(title = title, state = State(name = stateName), projectId = projectId, createdBy = currentUser.id)
                    .let { newTask ->
                        tasksRepository.addTask(newTask)
                        logsRepository.addLog(
                            CreatedLog(
                                username = currentUser.username,
                                affectedId = newTask.id,
                                affectedName = newTask.title,
                                affectedType = Log.AffectedType.TASK,
                            )
                        )
                    }
            }
        }
}