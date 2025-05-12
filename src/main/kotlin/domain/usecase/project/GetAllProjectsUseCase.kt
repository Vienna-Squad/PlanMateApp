package org.example.domain.usecase.project

import org.example.domain.NotFoundException
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke() = usersRepository.getCurrentUser().let { currentUser ->
        projectsRepository.getAllProjects()
            .filter { it.createdBy == currentUser.id }
            .ifEmpty { throw NotFoundException() }
    }
}