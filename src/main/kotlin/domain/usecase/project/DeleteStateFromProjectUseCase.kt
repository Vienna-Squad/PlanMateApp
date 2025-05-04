package domain.usecase.project

import org.example.domain.repository.ProjectsRepository
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteStateFromProjectUseCase(private val projectsRepository: ProjectsRepository = getKoin().get()) {
    operator fun invoke(projectId: UUID, state: String) = projectsRepository.deleteStateFromProject(
        projectId = projectId,
        state = state
    )
}
