package org.example.domain.usecase.project


import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*


class AddStateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(projectId: UUID, state: String) = projectsRepository.getProjectById(projectId).let { project ->
        projectsRepository.updateProject(project.copy(states = project.states + state))
        logsRepository.addLog(
            AddedLog(
                affectedId = state,
                affectedType = Log.AffectedType.STATE,
                addedTo = "project $projectId"
            )
        )
    }
}


