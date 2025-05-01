package org.example.domain.usecase.project

import org.example.domain.*
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository

class AddMateToProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(projectId: String, mateId: String) {


        val user = authenticationRepository.getCurrentUser().getOrElse {
            throw UnauthorizedException()
        }

        validateUserAuthorization(user)

        val project = projectsRepository.get(projectId).getOrElse {
            throw NoFoundException()
        }
        validateMateNotInProject(project, mateId)

        val updatedProject = updateProjectWithMate(project, mateId)

        projectsRepository.update(updatedProject).getOrElse {
            throw RuntimeException("Failed to update project", it)
        }

        createAndLogAction(updatedProject, mateId, user.username)
    }



    private fun validateUserAuthorization(user: User) {
        require(user.type != UserType.MATE) { throw AccessDeniedException() }
    }

    private fun validateMateNotInProject(project: Project, mateId: String) {
        require(!project.matesIds.contains(mateId)) { throw AlreadyExistException() }
    }

    private fun updateProjectWithMate(project: Project, mateId: String): Project {
        return project.copy(matesIds = project.matesIds + mateId)
    }

    private fun createAndLogAction(project: Project, mateId: String, username: String) {
        val log = AddedLog(
            username = username,
            affectedId = mateId,
            affectedType = Log.AffectedType.MATE,
            addedTo = project.id
        )
        val logResult = logsRepository.add(log)
        if (logResult.isFailure) {
            throw RuntimeException("Failed to log action")
        }
    }
}
