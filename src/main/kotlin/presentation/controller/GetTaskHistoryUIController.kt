package org.example.presentation.controller

import org.example.domain.entity.Log
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.viewer.ItemsViewer
import org.koin.java.KoinJavaComponent.getKoin

class GetTaskHistoryUIController(
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase=getKoin().get(),
    private val viewer: ItemsViewer<Log>,
    private val interactor: Interactor<String>

) : UiController {
    override fun execute() {
        tryAndShowError {
            println("Enter task id:")
            val taskId = interactor.getInput()
            viewer.view(getTaskHistoryUseCase.invoke(taskId))
        }
    }
}
