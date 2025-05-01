package org.example.presentation.controller

import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.java.KoinJavaComponent.getKoin

class GetProjectHistoryUiController(
    private val getProjectHistoryUseCase: GetProjectHistoryUseCase=getKoin().get(),
    private val stringInteractor:Interactor<String> = StringInteractor()

) : UiController {

    override fun execute() {
        tryAndShowError {

            println("enter your project id: ")
            val projectId = stringInteractor.getInput()

            val projectHistory = getProjectHistoryUseCase(projectId = projectId)
            if (projectHistory.isEmpty()) {
                println("No logs found for this project.")
            } else {
                println("Project History Logs:")
                projectHistory.forEach {
                    println(it)
                }
            }


        }
    }

}