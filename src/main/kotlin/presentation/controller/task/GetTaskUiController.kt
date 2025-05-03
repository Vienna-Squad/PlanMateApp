package org.example.presentation.controller.task

import org.example.domain.InvalidIdException
import org.example.domain.entity.Log
import org.example.domain.entity.Task
import org.example.domain.usecase.task.GetTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.koin.mp.KoinPlatform.getKoin
import java.util.*


class GetTaskUiController(
    private val getTaskUseCase: GetTaskUseCase= getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter task ID: ")
            val taskId = inputReader.getInput()
            require(taskId.isNotBlank()) {throw InvalidIdException()}
            println("Task retrieved: ${getTaskUseCase(UUID.fromString(taskId))}")
        }
    }
}


