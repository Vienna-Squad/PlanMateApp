package org.example.domain.usecase.project

import org.example.domain.entity.Project
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository


class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(name: String) =
        usersRepository.getCurrentUser().let { currentUser ->
            Project(name = name, createdBy = currentUser.id).let { newProject ->
                projectsRepository.addProject(newProject)
                logsRepository.addLog(
                    CreatedLog(
                        username = currentUser.username,
                        affectedId = newProject.id,
                        affectedName = name,
                        affectedType = Log.AffectedType.PROJECT
                    )
                )
            }
        }
}