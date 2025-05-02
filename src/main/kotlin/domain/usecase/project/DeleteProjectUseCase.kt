package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.UUID

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(projectId: UUID) {
        doIfAuthorized(authenticationRepository::getCurrentUser) { user ->
            if (user.type == UserType.MATE) throw AccessDeniedException()
            doIfExistedProject(projectId, projectsRepository::getProjectById) { project ->
                if (project.createdBy != user.id) throw AccessDeniedException()
                projectsRepository.deleteProjectById(project.id)
                logsRepository.addLog(
                    DeletedLog(
                        username = user.username,
                        affectedId = projectId,
                        affectedType = Log.AffectedType.PROJECT,
                    )
                )
            }
        }
    }

    private fun doIfAuthorized(getCurrentUser: () -> Result<User>, block: (User) -> Unit) {
        block(getCurrentUser().getOrElse { throw UnauthorizedException() })
    }

    private fun doIfExistedProject(
        projectId: UUID,
        getProject: (UUID) -> Result<Project>,
        block: (Project) -> Unit
    ) {
        block(getProject(projectId).getOrElse { throw if (projectId.toString().isBlank()) InvalidIdException() else NoFoundException() })
    }
}