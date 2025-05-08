package domain.usecase.project

import org.example.domain.NotFoundException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import java.util.*

class DeleteStateFromProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(projectId: UUID, state: String) = projectsRepository.getProjectById(projectId).let { project ->
        project.states.toMutableList().let { states ->
            if (!states.contains(state)) throw NotFoundException("state")
            states.remove(state)
            projectsRepository.updateProject(project.copy(states = states))
            logsRepository.addLog(
                DeletedLog(
                    username = usersRepository.getCurrentUser().username,
                    affectedId = state,
                    affectedType = Log.AffectedType.STATE,
                    deletedFrom = "project $projectId"
                )
            )
        }
    }
}
