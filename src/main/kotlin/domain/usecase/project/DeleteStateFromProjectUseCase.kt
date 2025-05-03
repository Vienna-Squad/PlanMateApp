package domain.usecase.project

import org.example.domain.*
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDateTime
import java.util.UUID

class DeleteStateFromProjectUseCase(
    private val authenticationRepository: AuthenticationRepository = getKoin().get(),
    private val projectsRepository: ProjectsRepository = getKoin().get(),
    private val logsRepository: LogsRepository = getKoin().get()
) {
    operator fun invoke(projectId: UUID, state: String) {
        authenticationRepository
            .getCurrentUser()
            .getOrElse {
                throw UnauthorizedException(
                    "User not found"
                )
            }.also { currentUser ->
                if (currentUser.type != UserType.ADMIN) {
                    throw AccessDeniedException(
                        "Only admins can delete states from projects"
                    )
                }
                projectsRepository.getProjectById(projectId)
                    .getOrElse {
                        throw NotFoundException(
                            "Project not found"
                        )
                    }
                    .also { project ->
                        if (project.createdBy != currentUser.id) throw AccessDeniedException(
                            "Only the creator of the project can delete states"
                        )
                        if (!project.states.contains(state)) throw NotFoundException(
                            "State with name $state not found in the project"
                        )  // state doesn't exist

                        projectsRepository.updateProject(
                            project.copy(
                                states = project.states - state
                            )
                        )
                    }

                logsRepository.addLog(
                    DeletedLog(
                        username = currentUser.username,
                        affectedId = projectId,
                        affectedType = Log.AffectedType.STATE,
                        dateTime = LocalDateTime.now(),
                        deletedFrom = projectId.toString(),
                    )
                ).getOrElse { throw FailedToLogException(
                    "Failed to log the deletion of state $state from project $projectId"
                ) }
            }
    }
}
