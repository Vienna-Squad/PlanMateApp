package org.example.presentation.controller.project

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetProjectHistoryUiController(
    private val getProjectHistoryUseCase: GetProjectHistoryUseCase = getKoin().get(),
    private val stringInputReader: InputReader<String> = StringInputReader(),
//    private val itemsViewer: ItemsViewer<Log>
) : UiController {
    override fun execute() {
        tryAndShowError {
            println("enter your project id: ")
            val projectId = stringInputReader.getInput()
            if (projectId.isEmpty()) throw InvalidIdException(
                "Project ID cannot be empty. Please provide a valid ID."
            )

            val projectHistory = getProjectHistoryUseCase(projectId = UUID.fromString(projectId))
            if (projectHistory.isEmpty()) {
                println("No logs found for this project.")
            } else {
                println("Project History Logs:")
//                itemsViewer.view(projectHistory)
                projectHistory.forEach { log ->
                    println(log)
                }

            }

        }
    }

}