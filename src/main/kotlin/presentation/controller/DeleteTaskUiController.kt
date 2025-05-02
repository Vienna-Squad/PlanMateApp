package org.example.presentation.controller

import org.example.domain.usecase.task.DeleteTaskUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin

class DeleteTaskUiController(
    private val deleteTaskUseCase: DeleteTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val interactor: Interactor<String> = StringInteractor()
) : UiController {
    override fun execute() {
        tryAndShowError {
            viewer.view("enter task ID to delete: ")
            val taskId = interactor.getInput()
            deleteTaskUseCase(taskId)
            viewer.view("the task #$taskId deleted.")
        }
    }
}