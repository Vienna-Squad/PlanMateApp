package org.example.presentation.controller.project

import org.example.domain.exceptions.InvalidInputException
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class AddStateToProjectUiController(
    private val addStateToProjectUseCase: AddStateToProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the new state to add: ")
            val newState = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            addStateToProjectUseCase(
                projectId = UUID.fromString(projectId),
                stateName = newState
            )
            viewer.view("State \"$newState\" was successfully added to Project [$projectId].\n")
        }
    }
}
