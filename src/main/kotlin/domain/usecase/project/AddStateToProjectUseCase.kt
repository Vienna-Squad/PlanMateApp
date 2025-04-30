package org.example.domain.usecase.project


import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.ProjectsRepository


class AddStateToProjectUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(projectId: String, state: String) {

    }
}
