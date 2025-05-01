package org.example.presentation.controller

import org.example.domain.usecase.task.GetTaskUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.mp.KoinPlatform.getKoin


class GetTaskUiController(
    private val getTaskUseCase: GetTaskUseCase= getKoin().get(),
    private val interactor: Interactor<String> = StringInteractor(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter task ID: ")
            val taskId = interactor.getInput()
            getTaskUseCase(taskId)
        }
    }
}