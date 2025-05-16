package org.example.domain.usecase.project

import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class DeleteStateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(projectId: UUID, stateName: String) {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        validator.canDeleteStateFromProject(project, currentUser)
        val stateToDelete = validator.getStateIfExistInProject(project, stateName)
        projectsRepository.updateProject(project.copy(states = project.states - stateToDelete))
        logsRepository.addLog(DeletedLog(
                username = currentUser.username,
                affectedId = stateToDelete.id,
                affectedName = stateName,
                affectedType = Log.AffectedType.STATE,
                deletedFrom = "project ${project.name} [$projectId]"
            ))
    }
}
