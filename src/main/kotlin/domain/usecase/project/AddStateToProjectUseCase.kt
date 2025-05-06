package org.example.domain.usecase.project


import org.example.domain.repository.ProjectsRepository
import org.koin.mp.KoinPlatform.getKoin
import java.util.*


class AddStateToProjectUseCase(private val projectsRepository: ProjectsRepository = getKoin().get()) {
    operator fun invoke(projectId: UUID, state: String) = projectsRepository.getProjectById(projectId).let { project ->
        projectsRepository.updateProject(project.copy(states = project.states + state))
    }
}


