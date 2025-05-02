package org.example.presentation.controller
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.viewer.ExceptionViewer

class EditTaskStateController(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val interactor: Interactor<String>,
    private val exceptionViewer: ExceptionViewer
) : UiController {
    override fun execute() {
        tryAndShowError(exceptionViewer) {
            print("enter task ID: ")
            val taskId = interactor.getInput()
            print("enter new state: ")
            val newState = interactor.getInput()

            editTaskStateUseCase(taskId, newState)
        }
    }
}