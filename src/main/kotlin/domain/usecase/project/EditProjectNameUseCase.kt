package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class EditProjectNameUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(projectId: UUID, name: String) {
        doIfAuthorized(authenticationRepository::getCurrentUser) { user ->
            if (user.type == UserType.MATE) throw AccessDeniedException(
                "Mates are not allowed to edit project names"
            )
            doIfExistedProject(projectId, projectsRepository::getProjectById) { project ->
                if (project.createdBy != user.id) throw AccessDeniedException(
                    "Only the creator of the project can edit it"
                )
                if (name != project.name) {
                    projectsRepository.updateProject(project.copy(name = name))
                    logsRepository.addLog(
                        ChangedLog(
                            username = user.username,
                            affectedId = projectId,
                            affectedType = Log.AffectedType.PROJECT,
                            changedFrom = project.name,
                            changedTo = name,
                        )
                    )
                }
            }
        }
    }

    private fun doIfAuthorized(getCurrentUser: () -> Result<User>, block: (User) -> Unit) {
        block(getCurrentUser().getOrElse {
            throw UnauthorizedException(
                "User not found"
            )
        })
    }

    private fun doIfExistedProject(
        projectId: UUID,
        getProject: (UUID) -> Result<Project>,
        block: (Project) -> Unit
    ) {
        block(getProject(projectId).getOrElse {
            throw if (projectId.toString().isBlank()) InvalidIdException(
                "Project ID is invalid"
            ) else NotFoundException(
                "Project with id $projectId not found"
            )
        }
        )
    }
}