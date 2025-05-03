package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.FailedToAddLogException
import org.example.domain.FailedToCreateProject
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*


class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(name: String, states: List<String>, matesIds: List<UUID>) {

        authenticationRepository.getCurrentUser().getOrElse { throw UnauthorizedException(
            "User not found"
        ) }.let { currentUser ->

            if (currentUser.type != UserType.ADMIN) {
                throw AccessDeniedException(
                    "Only admins can create projects"
                )
            }

            val newProject = Project(name = name, states = states, createdBy = currentUser.id, matesIds = matesIds)
            projectsRepository.addProject(newProject).getOrElse { throw FailedToCreateProject(
                "Failed to create project"
            ) }

            logsRepository.addLog(
                log = CreatedLog(
                    username = currentUser.username,
                    affectedType = Log.AffectedType.PROJECT,
                    affectedId = newProject.id
                )
            ).getOrElse { throw FailedToAddLogException(
                "Failed to add log"
            ) }
        }

    }
}