package org.example.presentation.controller.task
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class EditTaskStateController(
    private val editTaskStateUseCase: EditTaskStateUseCase=getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter task ID: ")
            val taskId = inputReader.getInput()
            print("enter new state: ")
            val newState = inputReader.getInput()
            editTaskStateUseCase(UUID.fromString( taskId), newState)
            println("task #$taskId state changed to $newState")
        }
    }
}