package org.example.presentation.controller.project

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class EditProjectStateUiController(
    private val editProjectStatesUseCase: EditProjectStatesUseCase=getKoin().get(),
    private val inputReader: InputReader<String> =StringInputReader(),
    private val itemViewer: ItemViewer<String> = StringViewer()
): UiController {
    override fun execute() {
        tryAndShowError {

            println("Enter Project Id to edit state: ")
            val projectIdInput=inputReader.getInput()
            if(projectIdInput.isEmpty())throw InvalidIdException(
                "Project ID cannot be empty. Please provide a valid ID."
            )

            println("Enter the new states separated by commas: ")
            val statesInput = inputReader.getInput()
            val states = statesInput.split(",").map { it.trim() }

            if (states.isEmpty()) throw InvalidIdException(
                "States cannot be empty. Please provide at least one state."
            )

            editProjectStatesUseCase(
                UUID.fromString( projectIdInput), states)
            itemViewer.view("Project states updated successfully")



        }
    }


}