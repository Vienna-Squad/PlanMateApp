package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.mp.KoinPlatform.getKoin

class AddStateToProjectUiController(
    private val addStateToProjectUseCase: AddStateToProjectUseCase= getKoin().get(),
    private val interactor: Interactor<String> = StringInteractor(),

    ) : UiController {

    override fun execute() {
        tryAndShowError {
            print("Enter project id")
            val projectId = interactor.getInput()
            if (!isValidInput(projectId)) throw InvalidIdException()
            print("Enter State you want to add")
            val newState = interactor.getInput()
            if (!isValidInput(newState)) throw InvalidIdException()
            addStateToProjectUseCase.invoke(
                projectId = projectId,
                state = newState
            )
            println("State added successfully")
        }

    }

    private fun isValidInput(input: String): Boolean {
        val regex = "^[A-Za-z]+$".toRegex()
        return regex.matches(input)
    }

}
