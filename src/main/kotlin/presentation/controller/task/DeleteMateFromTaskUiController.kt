package org.example.presentation.controller.task

import org.example.domain.InvalidIdException
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class DeleteMateFromTaskUiController(
    private val deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase =  getKoin().get(),
    private val stringInputReader: InputReader<String> = StringInputReader(),
    private val itemViewer: ItemViewer<String> = StringViewer()
) : UiController {

    override fun execute() {

        tryAndShowError() {

            println("enter your task id: ")
            val taskId = stringInputReader.getInput()
            if(taskId.isEmpty())throw InvalidIdException()

            println("enter your mate id to remove: ")
            val mateId = stringInputReader.getInput()
            if(mateId.isEmpty())throw InvalidIdException()

            deleteMateFromTaskUseCase(taskId = UUID.fromString( taskId), mate = UUID.fromString( mateId))
            itemViewer.view("mate deleted from task successfully")


        }
    }


}