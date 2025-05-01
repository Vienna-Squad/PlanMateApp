package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.entity.Log
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ItemsViewer
import org.koin.java.KoinJavaComponent.getKoin

class GetProjectHistoryUiController(
    private val getProjectHistoryUseCase: GetProjectHistoryUseCase = getKoin().get(),
    private val stringInteractor: Interactor<String> = StringInteractor(),
//    private val itemsViewer: ItemsViewer<Log>

) : UiController {

    override fun execute() {
        tryAndShowError {

            println("enter your project id: ")
            val projectId = stringInteractor.getInput()
            if (projectId.isEmpty()) throw InvalidIdException()

            val projectHistory = getProjectHistoryUseCase(projectId = projectId)
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