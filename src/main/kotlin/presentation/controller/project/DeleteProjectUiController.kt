package org.example.presentation.controller.project

import org.example.domain.InvalidInputException
import org.example.domain.usecase.project.DeleteProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteProjectUiController(
    private val deleteProjectUseCase: DeleteProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Project ID cannot be empty. Please provide a valid ID.")
            }
            deleteProjectUseCase(UUID.fromString(projectId))
            viewer.view("Project with ID $projectId has been successfully deleted.\n")
        }
    }
}