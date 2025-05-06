package org.example.domain.usecase.project

import org.example.domain.NoChangeException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import java.util.*

class EditProjectNameUseCase(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
) {
    operator fun invoke(projectId: UUID, newName: String) =
        projectsRepository.getProjectById(projectId).let { project ->
            if (project.name == newName) throw NoChangeException()
            projectsRepository.updateProject(project.copy(name = newName))
            logsRepository.addLog(
                ChangedLog(
                    affectedId = projectId.toString(),
                    affectedType = Log.AffectedType.PROJECT,
                    changedFrom = project.name,
                    changedTo = newName
                )
            )
        }
}