package org.example.domain.usecase.project

import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(projectId: UUID, mateId: UUID) {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        val mate = usersRepository.getUserByID(mateId)
        validator.canAddMateToProject(project, currentUser, mateId)
        projectsRepository.updateProject(project.copy(matesIds = project.matesIds + mate.id))
        logsRepository.addLog(AddedLog(
                username = currentUser.username,
                affectedId = mateId,
                affectedName = mate.username,
                affectedType = Log.AffectedType.MATE,
                addedTo = "project ${project.name} [$projectId]"
            ))
    }
}