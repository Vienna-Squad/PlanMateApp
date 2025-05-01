package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class DeleteMateFromTaskUiController(
    private val deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase =  getKoin().get(),
    private val stringInteractor: Interactor<String> = StringInteractor(),
    private val itemViewer: ItemViewer<String> = StringViewer()
) : UiController {

    override fun execute() {

        tryAndShowError() {

            println("enter your task id: ")
            val taskId = stringInteractor.getInput()
            if(taskId.isEmpty())throw InvalidIdException()

            println("enter your mate id to remove: ")
            val mateId = stringInteractor.getInput()
            if(mateId.isEmpty())throw InvalidIdException()

            deleteMateFromTaskUseCase(taskId = taskId, mate = mateId)
            itemViewer.view("mate deleted from task successfully")


        }
    }


}