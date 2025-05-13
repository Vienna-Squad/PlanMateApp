package org.example.presentation.controller.task

import org.example.domain.InvalidInputException
import org.example.domain.usecase.task.DeleteTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteTaskUiController(
    private val deleteTaskUseCase: DeleteTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the Task ID to delete: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            deleteTaskUseCase(UUID.fromString(taskId))
            viewer.view("Task with ID #$taskId has been successfully deleted.\n")
        }
    }
}