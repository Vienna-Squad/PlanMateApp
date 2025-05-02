package org.example.presentation.controller.project

import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.viewer.ExceptionViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class EditProjectStatesController(
    private val editProjectStatesUseCase: EditProjectStatesUseCase= getKoin().get(),
    private val inputReader: InputReader<String>,
    private val exceptionViewer: ExceptionViewer
) : UiController {
    override fun execute() {
        tryAndShowError(exceptionViewer) {
            print("enter project ID: ")
            val projectId = inputReader.getInput()
            print("enter new states (comma-separated): ")
            val statesInput = inputReader.getInput()

            val newStates = statesInput.split(",").map { it.trim() }
            editProjectStatesUseCase(UUID.fromString( projectId), newStates)
        }
    }
}