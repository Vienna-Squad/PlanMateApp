package org.example.presentation.controller

import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.viewer.ExceptionViewer

class EditProjectStatesController(
    private val editProjectStatesUseCase: EditProjectStatesUseCase,
    private val interactor: Interactor<String>,
    private val exceptionViewer: ExceptionViewer
) : UiController {
    override fun execute() {
        tryAndShowError(exceptionViewer) {
            print("enter project ID: ")
            val projectId = interactor.getInput()
            print("enter new states (comma-separated): ")
            val statesInput = interactor.getInput()

            val newStates = statesInput.split(",").map { it.trim() }
            editProjectStatesUseCase(projectId, newStates)
        }
    }
}