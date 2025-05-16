package org.example.domain.usecase.project

import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import java.util.*

class DeleteMateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
    private val validator: Validator
) {
    operator fun invoke(projectId: UUID, mateId: UUID) {
        val currentUser = usersRepository.getCurrentUser()
        val project = projectsRepository.getProjectById(projectId)
        validator.canDeleteMateFromProject(project, currentUser, mateId)
        val mate = usersRepository.getUserByID(mateId)
        projectsRepository.updateProject(project.copy(matesIds = project.matesIds - mateId))
        logsRepository.addLog(DeletedLog(
                username = currentUser.username,
                affectedId = mateId,
                affectedName = mate.username,
                affectedType = Log.AffectedType.MATE,
                deletedFrom = "project ${project.name} [$projectId]"
            ))
    }
}