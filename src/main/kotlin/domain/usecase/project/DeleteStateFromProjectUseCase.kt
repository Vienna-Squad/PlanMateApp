package domain.usecase.project

import org.example.domain.NotFoundException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.koin.mp.KoinPlatform.getKoin
import java.util.*
import kotlin.math.log

class DeleteStateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository = getKoin().get(),
    private val logsRepository: LogsRepository = getKoin().get(),
    private val authRepository: AuthRepository= getKoin().get()
) {
    operator fun invoke(projectId: UUID, state: String) {
        projectsRepository.deleteStateFromProject(
            projectId = projectId,
            state = state

        )
        logsRepository.addLog(
            DeletedLog(
                username = authRepository.getCurrentUser()?.username.let { throw NotFoundException("User") },
                affectedId = projectId ,
                affectedType = Log.AffectedType.PROJECT,
                deletedFrom = "Project $projectId",
            )
        )

    }
}
