package org.example.presentation.controller.task

import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetTaskHistoryUIController(
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader()

) : UiController {
    override fun execute() {
        tryAndShowError {
            println("Enter task id:")
            val taskId = inputReader.getInput()
            getTaskHistoryUseCase.invoke(UUID.fromString(taskId))
                .onSuccess {
                    viewer.view("Task title updated successfully.")
                }.exceptionOrNull()
        }
    }
}
