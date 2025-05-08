package org.example.domain.usecase.project


import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*


class AddStateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, state: String) = projectsRepository.getProjectById(projectId).let { project ->
        projectsRepository.updateProject(project.copy(states = project.states + state))
        logsRepository.addLog(
            AddedLog(
                username = usersRepository.getCurrentUser().username,
                affectedId = state,
                affectedType = Log.AffectedType.STATE,
                addedTo = "project $projectId"
            )
        )
    }
}


