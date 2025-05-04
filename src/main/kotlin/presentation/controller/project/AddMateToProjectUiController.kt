package org.example.presentation.controller.project

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class AddMateToProjectUiController(
    private val addMateToProjectUseCase: AddMateToProjectUseCase = getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader(),
    private val stringViewer: ItemViewer<String> = StringViewer()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter mate ID: ")
            val mateId = inputReader.getInput()
            require(mateId.isNotBlank()) { throw InvalidIdException("Mate ID cannot be blank. Please provide a valid ID.") }
            print("enter project ID: ")
            val projectId = inputReader.getInput()
            require(projectId.isNotBlank()) { throw InvalidIdException("Project ID cannot be blank. Please provide a valid ID.") }
            addMateToProjectUseCase(UUID.fromString(projectId), UUID.fromString(mateId))
                .onSuccess { stringViewer.view("mate #$mateId added successfully!!") }
                .onFailure { throw it }
        }

    }
}