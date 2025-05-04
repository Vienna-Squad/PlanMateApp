package org.example.domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import java.util.*

class DeleteMateFromProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID, mateId: UUID) = projectsRepository.deleteMateFromProject(
        projectId = projectId,
        mateId = mateId
    )
}