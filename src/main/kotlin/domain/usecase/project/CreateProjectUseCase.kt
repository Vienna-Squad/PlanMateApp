package org.example.domain.usecase.project

import org.example.domain.FailedToAddProjectException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository


class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(name: String, states: List<String>, creatorId: String, matesIds: List<String> = emptyList()) {

        val currentUser = authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException() }

        if (currentUser.type != UserType.ADMIN) {
            throw UnauthorizedException()
        }

        val newProject = Project(name, states, creatorId, matesIds)
        projectsRepository.add(newProject).getOrElse { throw FailedToAddProjectException() }

        logsRepository.add(
            log = CreatedLog(
                username = currentUser.username,
                affectedType = Log.AffectedType.PROJECT,
                affectedId = newProject.id
            )
        ).getOrElse { throw FailedToAddProjectException() }

    }
}