package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemsViewer

class AddStateToProjectUiController(
    private val addStateToProjectUseCase: AddStateToProjectUseCase,
    private val interactor: Interactor<String>,
    private val viewer: ItemsViewer<String>,
    private val exceptionViewer: ExceptionViewer
) : UiController {
    override fun execute() {
        print("Enter project id")
        val projectId = interactor.getInput()
        if (!isValidInput(projectId)) throw InvalidIdException()
        print("Enter State you want to add")
        val newState = interactor.getInput()
        if (!isValidInput(newState)) throw InvalidIdException()
        try {
            addStateToProjectUseCase.invoke(
                projectId = projectId,
                state = newState
            )
            println("State added successfully")
        } catch (e: Exception) {
            exceptionViewer.view(e)
        }
    }
    private fun isValidInput(input: String): Boolean {
        val regex = "^[A-Za-z]+$".toRegex()
        return regex.matches(input)
    }


}