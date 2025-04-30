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

        validateInputs(projectId, mateId)

        val userResult = authenticationRepository.getCurrentUser()
        if (userResult.isFailure) {
            throw UnauthorizedException()
        }
        val user = userResult.getOrThrow()
        validateUserAuthorization(user)

        val projectResult = projectsRepository.get(projectId)
        if (projectResult.isFailure) {
            throw NoFoundException()
        }
        val project = projectResult.getOrThrow()
        validateMateNotInProject(project, mateId)

        val updatedProject = updateProjectWithMate(project, mateId)

        val updateResult = projectsRepository.update(updatedProject)
        if (updateResult.isFailure) {
            throw RuntimeException("Failed to update project")
        }

        createAndLogAction(updatedProject, mateId, user.username)
    }

    private fun validateInputs(projectId: String, mateId: String) {
        require(projectId.isNotBlank()) { throw InvalidIdException() }
        require(mateId.isNotBlank()) { throw InvalidIdException() }
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
