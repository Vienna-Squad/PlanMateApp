package org.example.presentation.controller

import org.example.domain.usecase.task.GetTaskUseCase
import org.example.presentation.utils.interactor.Interactor


class GetTaskUiController(
    private val getTaskUseCase: GetTaskUseCase,
    private val interactor: Interactor<String>,
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter task ID: ")
            val taskId = interactor.getInput()
            getTaskUseCase(taskId)
        }
    }
}