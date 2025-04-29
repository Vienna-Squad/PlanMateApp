package org.example.domain.usecase.project
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository


class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository
) {
    operator fun invoke(
        name: String,
        states: List<String>,
        creatorId: String,
        matesIds: List<String> = emptyList()
    ) {
    }
}