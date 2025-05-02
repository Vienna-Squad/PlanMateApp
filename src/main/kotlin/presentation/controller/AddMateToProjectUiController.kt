package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin

class AddMateToProjectUiController(
    private val addMateToProjectUseCase: AddMateToProjectUseCase= getKoin().get(),
    private val interactor: Interactor<String> = StringInteractor(),
    private val stringViewer: ItemViewer<String> = StringViewer()
) : UiController {
    override fun execute() {
        tryAndShowError {
            println("enter mate ID: ")
            val mateId = interactor.getInput()
            require(mateId.isNotBlank()) { throw InvalidIdException() }
            println("enter project ID: ")
            val projectId = interactor.getInput()
            require(projectId.isNotBlank()) { throw InvalidIdException() }
            addMateToProjectUseCase(projectId, mateId)
            stringViewer.view("The Mate has been added successfully")
        }

    }
}