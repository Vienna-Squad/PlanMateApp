package org.example.presentation.controller.project

import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetProjectHistoryUiController(
    private val getProjectHistoryUseCase: GetProjectHistoryUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val stringInputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter your project id: ")
            val projectId = stringInputReader.getInput()
            getProjectHistoryUseCase(projectId = UUID.fromString(projectId))
                .onSuccess { logs ->
                    logs.forEach { log -> viewer.view(log.toString()) }
                }.exceptionOrNull()
        }
    }
}