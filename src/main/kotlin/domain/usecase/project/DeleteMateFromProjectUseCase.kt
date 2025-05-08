package org.example.domain.usecase.project

import org.example.domain.NotFoundException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*
import kotlin.coroutines.CoroutineContext

class DeleteMateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, mateId: UUID) = projectsRepository.getProjectById(projectId).let { project ->
        project.matesIds.toMutableList().let { matesIds ->
            if (!matesIds.contains(mateId)) throw NotFoundException("mate")
            matesIds.remove(mateId)
            projectsRepository.updateProject(project.copy(matesIds = matesIds))
            logsRepository.addLog(
                DeletedLog(
                    username = usersRepository.getCurrentUser().username,
                    affectedId = mateId.toString(),
                    affectedType = Log.AffectedType.MATE,
                    deletedFrom = "project $projectId"
                )
            )
        }
    }
}