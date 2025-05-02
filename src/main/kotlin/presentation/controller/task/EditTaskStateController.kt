package org.example.presentation.controller.task
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.viewer.ExceptionViewer
import java.util.*

class EditTaskStateController(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val inputReader: InputReader<String>,
    private val exceptionViewer: ExceptionViewer
) : UiController {
    override fun execute() {
        tryAndShowError(exceptionViewer) {
            print("enter task ID: ")
            val taskId = inputReader.getInput()
            print("enter new state: ")
            val newState = inputReader.getInput()

            editTaskStateUseCase(UUID.fromString( taskId), newState)
        }
    }
}