package org.example.domain.usecase.project

import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository

class DeleteMateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(projectId: String, mateId: String) {
        doIfAuthorized(authenticationRepository::getCurrentUser) { user ->
            if (user.type == UserType.MATE) throw AccessDeniedException()
            doIfExistedProject(projectId, projectsRepository::get) { project ->
                if (project.createdBy != user.id) throw AccessDeniedException()
                doIfExistedMate(mateId, authenticationRepository::getUser) { mate ->
                    if (!project.matesIds.contains(mateId)) throw NoFoundException()
                    projectsRepository.update(
                        project.copy(
                            matesIds = project.matesIds.toMutableList().apply { remove(mateId) })
                    )
                    logsRepository.add(
                        DeletedLog(
                            username = user.username,
                            affectedId = projectId,
                            affectedType = Log.AffectedType.MATE,
                            deletedFrom = "project $projectId",
                        )
                    )
                }
            }
        }
    }

    private fun doIfAuthorized(getCurrentUser: () -> Result<User>, block: (User) -> Unit) {
        block(getCurrentUser().getOrElse { throw UnauthorizedException() })
    }

    private fun doIfExistedProject(
        projectId: String,
        getProject: (String) -> Result<Project>,
        block: (Project) -> Unit
    ) {
        block(getProject(projectId).getOrElse { throw if (projectId.isBlank()) InvalidIdException() else NoFoundException() })
    }

    private fun doIfExistedMate(userId: String, getUser: (userId: String) -> Result<User>, block: (User) -> Unit) {
        block(getUser(userId).getOrElse { throw NoFoundException() })
    }
}