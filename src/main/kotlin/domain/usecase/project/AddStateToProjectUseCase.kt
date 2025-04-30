package org.example.domain.usecase.project


import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.time.LocalDateTime


class AddStateToProjectUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(projectId: String, state: String) {
        authenticationRepository.getCurrentUser().getOrNull()?.let { currentUser ->
            if (currentUser.type != UserType.ADMIN) {
                throw UnauthorizedException()
            }

            projectsRepository.getAll().getOrNull()?.let { projects ->
                val project = projects.firstOrNull { project ->
                    project.id == projectId
                } ?: throw NoFoundException()
                projectsRepository.update(
                    project.copy(states = project.states + state)
                )
                logsRepository.add(
                    ChangedLog(
                        username = currentUser.username,
                        affectedId = projectId,
                        affectedType = Log.AffectedType.STATE,
                        dateTime = LocalDateTime.now(),
                        changedFrom = project.states.toString(),
                        changedTo = (project.states + state).toString(),
                    )
                )

            } ?: throw NoFoundException()
        } ?: throw NoFoundException()

    }
}

