package org.example.presentation.controller.task

import org.example.domain.InvalidInputException
import org.example.domain.entity.log.Log
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.ItemsViewer
import org.example.presentation.utils.viewer.LogsViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetTaskHistoryUIController(
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
    private val logsViewer: ItemsViewer<Log> = LogsViewer(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the Task ID: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Task ID cannot be empty. Please provide a valid ID.")
            }
            getTaskHistoryUseCase(UUID.fromString(taskId)).also { logs ->
                viewer.view("History for Task #$taskId:\n")
                logsViewer.view(logs)
            }
        }
    }
}
