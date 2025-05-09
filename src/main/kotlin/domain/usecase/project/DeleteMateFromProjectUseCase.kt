package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteMateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) =
        usersRepository.getCurrentUser().let { currentUser ->
            projectsRepository.getProjectById(projectId).let { project ->
                if (project.createdBy != currentUser.id) throw AccessDeniedException("project")
                usersRepository.getUserByID(mateId).let { mate ->
                    if (!project.matesIds.contains(mate.id)) throw ProjectHasNoException("mate")
                    project.matesIds.toMutableList().let { matesIds ->
                        matesIds.remove(mateId)
                        projectsRepository.updateProject(project.copy(matesIds = matesIds))
                        logsRepository.addLog(
                            DeletedLog(
                                username = currentUser.username,
                                affectedId = mateId,
                                affectedName = mate.username,
                                affectedType = Log.AffectedType.MATE,
                                deletedFrom = "project ${project.name} [$projectId]"
                            )
                        )
                    }
                }
            }
        }
}