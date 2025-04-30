package org.example.presentation.controller

import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.example.presentation.utils.interactor.Interactor

class GetTaskHistoryUseCaseUIController (
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase,
    private val interactor: Interactor<String>

):UiController{
    override fun execute() {
        println("Enter task id:")
        val taskId=interactor.getInput()
        try {
            getTaskHistoryUseCase.invoke(taskId)
        }catch (e:Exception){
            println("Error: ${e.message}")
        }
    }

}