package org.example.domain.usecase.project

import org.example.domain.*
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.UUID

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(projectId: UUID, mateId: UUID) {


        val user = authenticationRepository.getCurrentUser().getOrElse {
            throw UnauthorizedException(
                "User not found"
            )
        }

        validateUserAuthorization(user)

        val project = projectsRepository.getProjectById(projectId).getOrElse {
            throw NotFoundException(
                "Project not found"
            )
        }
        validateMateNotInProject(project, mateId)

        val updatedProject = updateProjectWithMate(project, mateId)

        projectsRepository.updateProject(updatedProject).getOrElse {
            throw RuntimeException("Failed to update project", it)
        }

        createAndLogAction(updatedProject, mateId, user.username)
    }



    private fun validateUserAuthorization(user: User) {
        require(user.type != UserType.MATE) { throw AccessDeniedException(
            "Mates are not allowed to add other mates to projects"
        ) }
    }

    private fun validateMateNotInProject(project: Project, mateId: UUID) {
        require(!project.matesIds.contains(mateId)) { throw AlreadyExistException(
            "Mate is already in the project"
        ) }
    }

    private fun updateProjectWithMate(project: Project, mateId: UUID): Project {
        return project.copy(matesIds = project.matesIds + mateId)
    }

    private fun createAndLogAction(project: Project, mateId: UUID, username: String) {
        val log = AddedLog(
            username = username,
            affectedId = mateId,
            affectedType = Log.AffectedType.MATE,
            addedTo = project.id
        )
        val logResult = logsRepository.addLog(log)
        if (logResult.isFailure) {
            throw RuntimeException("Failed to log action")
        }
    }
}
