package org.example.presentation.controller.project

import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class AddStateToProjectUiController(
    private val addStateToProjectUseCase: AddStateToProjectUseCase= getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader(),
    ) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Enter project id")
            val projectId = inputReader.getInput()
            print("Enter State you want to add")
            val newState = inputReader.getInput()
            addStateToProjectUseCase.invoke(
                projectId = UUID.fromString( projectId),
                state = newState
            )
            println("State added successfully")
        }

    }


}
