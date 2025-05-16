package org.example.domain.usecase.project

import org.example.domain.entity.Project
import org.example.domain.exceptions.NoProjectsFoundException
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(): List<Project> {
        val currentUser = usersRepository.getCurrentUser()
        return projectsRepository.getAllProjects()
            .filter { it.createdBy == currentUser.id }
            .ifEmpty { throw NoProjectsFoundException() }
    }
}