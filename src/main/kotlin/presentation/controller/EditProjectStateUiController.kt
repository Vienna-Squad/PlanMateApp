package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.ItemsViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class EditProjectStateUiController(
    private val editProjectStatesUseCase: EditProjectStatesUseCase=getKoin().get(),
    private val interactor: Interactor<String> =StringInteractor(),
    private val itemViewer: ItemViewer<String> = StringViewer()
):UiController {
    override fun execute() {
        tryAndShowError {

            println("Enter Project Id to edit state: ")
            val projectIdInput=interactor.getInput()
            if(projectIdInput.isEmpty())throw InvalidIdException()

            println("Enter the new states separated by commas: ")
            val statesInput = interactor.getInput()
            val states = statesInput.split(",").map { it.trim() }

            if (states.isEmpty()) throw InvalidIdException()

            editProjectStatesUseCase(projectIdInput, states)
            itemViewer.view("Project states updated successfully")



        }
    }


}