package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.FailedToAddLogException
import org.example.domain.FailedToCreateProject
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
    operator fun invoke(name: String, states: List<String>, creatorId: String, matesIds: List<String>) {

        val currentUser = authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException() }

        if (currentUser.type != UserType.ADMIN) {
            throw AccessDeniedException()
        }

        val newProject = Project(name=name, states =  states, createdBy =  creatorId, matesIds =  matesIds)
        projectsRepository.add(newProject).getOrElse { throw FailedToCreateProject() }

        logsRepository.add(
            log = CreatedLog(
                username = currentUser.username,
                affectedType = Log.AffectedType.PROJECT,
                affectedId = newProject.id
            )
        ).getOrElse { throw FailedToAddLogException() }

    }
}