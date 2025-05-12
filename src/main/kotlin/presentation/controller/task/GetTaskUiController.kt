package org.example.presentation.controller.task

import org.example.domain.InvalidInputException
import org.example.domain.usecase.task.GetTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class GetTaskUiController(
    private val getTaskUseCase: GetTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the Task ID: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            getTaskUseCase(UUID.fromString(taskId)).also { task ->
                viewer.view("Task retrieved successfully: Task ID #$taskId\n")
                println(task.toString())
            }
        }
    }
}


