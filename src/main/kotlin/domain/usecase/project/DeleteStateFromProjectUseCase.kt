package org.example.domain.usecase.project


import org.example.domain.ProjectAccessDenied
import org.example.domain.StateNotInProjectException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteStateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, stateName: String) {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        if (project.createdBy != currentUser.id) throw ProjectAccessDenied()
        val stateToDelete = project.states.find { it.name == stateName } ?: throw StateNotInProjectException()
        projectsRepository.updateProject(project.copy(states = project.states - stateToDelete))
        logsRepository.addLog(
            DeletedLog(
                username = currentUser.username,
                affectedId = stateToDelete.id,
                affectedName = stateName,
                affectedType = Log.AffectedType.STATE,
                deletedFrom = "project ${project.name} [$projectId]"
            )
        )
    }
}
