package org.example.presentation.controller

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class AddMateToTaskController(
    private val addMateToTaskUseCase: AddMateToTaskUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val interactor: Interactor<String> = StringInteractor(),
    private val exceptionViewer: ItemViewer<PlanMateAppException> = ExceptionViewer()

): UiController {
    override fun execute() {
        tryAndShowError(exceptionViewer){
            println("enter task ID: ")
            val taskId = interactor.getInput()
            println("enter mate ID: ")
            val mateId = interactor.getInput()
            addMateToTaskUseCase(taskId, mateId)
            stringViewer.view("Mate: $mateId added to task: $taskId successfully")
        }
    }

}