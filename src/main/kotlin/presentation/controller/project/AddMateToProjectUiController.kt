package org.example.presentation.controller.project

import org.example.domain.InvalidInputException
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class AddMateToProjectUiController(
    private val addMateToProjectUseCase: AddMateToProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the mate ID: ")
            val mateId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            addMateToProjectUseCase(UUID.fromString(projectId), UUID.fromString(mateId))
            viewer.view("Mate with ID [$mateId] was successfully added to project [$projectId].\n")
        }
    }
}