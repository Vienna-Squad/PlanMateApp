package org.example.presentation.controller.project

import org.example.domain.InvalidInputException
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteMateFromProjectUiController(
    private val deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Project ID cannot be blank. Please provide a valid ID.")
            }
            print("Please enter Mate ID: ")
            val mateId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Mate ID cannot be blank. Please provide a valid ID.")
            }
            deleteMateFromProjectUseCase(UUID.fromString(projectId), UUID.fromString(mateId))
            viewer.view("Mate [$mateId] has been successfully removed from project [$projectId].\n")
        }
    }
}