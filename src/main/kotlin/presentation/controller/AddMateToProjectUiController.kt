package org.example.presentation.controller

import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.example.presentation.utils.interactor.Interactor

class AddMateToProjectUiController(
    private val addMateToProjectUseCase: AddMateToProjectUseCase,
    private val interactor: Interactor<String>,
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter mate ID: ")
            val mateId = interactor.getInput()
            print("enter project ID: ")
            val projectId = interactor.getInput()
            addMateToProjectUseCase(mateId, projectId)
        }
    }
}