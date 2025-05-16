package org.example.domain.usecase.project

import org.example.domain.entity.State
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class AddStateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(projectId: UUID, stateName: String) {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        val state = State(name = stateName)
        validator.canAddStateToProject(project,currentUser,stateName)
        projectsRepository.updateProject(project.copy(states = project.states + state))
        logsRepository.addLog(AddedLog(
                username = currentUser.username,
                affectedId = state.id,
                affectedName = stateName,
                affectedType = Log.AffectedType.STATE,
                addedTo = "project ${project.name} [$projectId]"
            ))
    }
}