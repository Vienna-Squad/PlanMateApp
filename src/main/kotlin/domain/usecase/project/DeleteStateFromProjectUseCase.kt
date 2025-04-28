package domain.usecase.project

import domain.repository.StatesRepository

class DeleteStateFromProjectUseCase(
    private val statesRepository: StatesRepository
) {
    operator fun invoke(projectId: String, state: String) : Boolean {
        return  statesRepository.deleteStateFromProject(projectId, state)
    }
}