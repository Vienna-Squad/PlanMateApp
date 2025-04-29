package org.example.domain.usecase.project

import org.example.domain.InvalidMateIdException
import org.example.domain.InvalidProjectIdException
import org.example.domain.MateAlreadyInProjectException
import org.example.domain.NoProjectFoundException
import org.example.domain.entity.AddedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository
) {

    operator fun invoke(projectId: String, mateId: String, username: String) {
        if (projectId.isBlank()) throw InvalidProjectIdException()
        if (mateId.isBlank()) throw InvalidMateIdException()

        val projectResult = projectsRepository.get(projectId)
        if (projectResult.isFailure) {
            throw NoProjectFoundException()
        }
        val project = projectResult.getOrThrow()

        if (project.matesIds.contains(mateId)) {
            throw MateAlreadyInProjectException()
        }

        val updatedMates = project.matesIds.toMutableList().apply { add(mateId) }
        val updatedProject = project.copy(matesIds = updatedMates)

        projectsRepository.update(updatedProject).getOrThrow()

        val log = AddedLog(
            username = username,
            affectedId = mateId,
            affectedType = Log.AffectedType.MATE,
            addedTo = projectId
        )
        logsRepository.add(log).getOrThrow()
    }
}
