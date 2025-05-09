package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) =
        usersRepository.getCurrentUser().let { currentUser ->
            projectsRepository.getProjectById(projectId).let { project ->
                if (project.createdBy != currentUser.id) throw AccessDeniedException("project")
                usersRepository.getUserByID(mateId).let { mate ->
                    if (project.matesIds.contains(mate.id)) throw AlreadyExistException("mate")
                    projectsRepository.updateProject(project.copy(matesIds = project.matesIds + mate.id))
                    logsRepository.addLog(
                        AddedLog(
                            username = currentUser.username,
                            affectedId = mateId,
                            affectedName = mate.username,
                            affectedType = Log.AffectedType.MATE,
                            addedTo = "project ${project.name} [$projectId]"
                        )
                    )
                }
            }
        }
}