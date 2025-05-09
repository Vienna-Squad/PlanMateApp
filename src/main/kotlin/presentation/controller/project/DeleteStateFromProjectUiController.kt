package org.example.presentation.controller.project


import domain.usecase.project.DeleteStateFromProjectUseCase
import org.example.domain.InvalidInputException
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteStateFromProjectUiController(
    private val deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Project ID cannot be empty. Please enter a valid ID.")
            }
            print("Enter the state you want to delete: ")
            val stateToDelete = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("State name cannot be empty. Please enter a valid state.")
            }
            deleteStateFromProjectUseCase(
                projectId = UUID.fromString(projectId),
                stateName = stateToDelete
            )
            viewer.view("State \"$stateToDelete\" has been successfully removed from the project.\n")
        }

    }
}
