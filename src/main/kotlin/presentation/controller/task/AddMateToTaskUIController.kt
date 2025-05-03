package org.example.presentation.controller.task

import org.example.domain.InvalidIdException
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class AddMateToTaskUIController(
    private val addMateToTaskUseCase: AddMateToTaskUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),

    ): UiController {
    override fun execute() {
        tryAndShowError{
            println("enter task ID: ")
            val taskId = inputReader.getInput()
            if (taskId.isBlank()) {
                throw InvalidIdException(
                    "Task ID cannot be blank. Please provide a valid ID."
                )
            }
            println("enter mate ID: ")
            val mateId = inputReader.getInput()
            if (mateId.isBlank()) {
                throw InvalidIdException(
                    "Mate ID cannot be blank. Please provide a valid ID."
                )
            }
            addMateToTaskUseCase(UUID.fromString( taskId), UUID.fromString(  mateId))
            stringViewer.view("Mate: $mateId added to task: $taskId successfully")
        }

    }

}