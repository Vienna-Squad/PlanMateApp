package org.example.domain.usecase.project

import org.example.domain.NotFoundException
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.ProjectsRepository
import org.koin.java.KoinJavaComponent.getKoin


class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository=getKoin().get(),
    private val authRepository: AuthRepository=getKoin().get(),
) {
    operator fun invoke(name: String) {
        projectsRepository.addProject(name)
        authRepository.getCurrentUser()?.let { user ->
            projectsRepository.addMateToProject(
                projectId = projectsRepository.getAllProjects().last().id,
                mateId = user.id
            )
        } ?: throw NotFoundException("User")
    }
}