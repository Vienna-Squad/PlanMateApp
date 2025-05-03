package org.example.presentation.controller.task

import org.example.domain.entity.Log
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemsViewer
import org.example.presentation.utils.viewer.LogsViewer
import org.example.presentation.utils.viewer.TaskHistoryViewer
import org.koin.java.KoinJavaComponent.getKoin

class GetTaskHistoryUIController(
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase=getKoin().get(),
    private val viewer: ItemsViewer<Log> = TaskHistoryViewer(),
    private val inputReader: InputReader<String> = StringInputReader()

) : UiController {
    override fun execute() {
        tryAndShowError {
            println("Enter task id:")
            val taskId = inputReader.getInput()
            viewer.view(getTaskHistoryUseCase.invoke(taskId))
        }
    }
}
