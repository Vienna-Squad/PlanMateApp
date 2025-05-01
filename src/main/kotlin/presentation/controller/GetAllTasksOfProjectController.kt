package org.example.presentation.controller

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class GetAllTasksOfProjectController(
    private val getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val interactor: Interactor<String> = StringInteractor(),
    private val exceptionViewer: ItemViewer<PlanMateAppException> = ExceptionViewer()
): UiController {
    override fun execute() {
        tryAndShowError(exceptionViewer){
            println("enter project ID: ")
            val projectId = interactor.getInput()
            val tasks = getAllTasksOfProjectUseCase(projectId)
            stringViewer.view(tasks.toString())
        }

    }
}