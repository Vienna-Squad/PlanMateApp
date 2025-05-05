package org.example.presentation.controller.project

import org.example.domain.InvalidInputException
import org.example.domain.entity.Log
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.ItemsViewer
import org.example.presentation.utils.viewer.LogsViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetProjectHistoryUiController(
    private val getProjectHistoryUseCase: GetProjectHistoryUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
    private val logsViewer: ItemsViewer<Log> = LogsViewer()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Project ID cannot be blank. Please enter a valid ID.")
            }
            getProjectHistoryUseCase(projectId = UUID.fromString(projectId)).let { logs ->
                viewer.view("History for project ID $projectId:\n")
                logsViewer.view(logs)
            }
        }
    }
}